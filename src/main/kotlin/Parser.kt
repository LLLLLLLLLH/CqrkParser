import bean.CourseInfo
import bean.Info
import org.jsoup.Jsoup


/**
 * 网页解析需要传入url地址和Cookie
 * @author Binding
 * @date 2023/08/19
 * @constructor 创建[Parser]
 * @param [url]
 * @param [cookie]
 */
class Parser(private val url: String, private val cookie: String) {

    private var data = mutableSetOf<Info>()
    private var courseInfoList: MutableList<CourseInfo> = mutableListOf()

    /**
     * 解析学期每周的课程并返回
     * @return 返回封装好的数据
     */
    fun convert(): MutableSet<Info> {
        SslUtils.trustEveryone()
        for (i in 1..17) {
            val currentUrl = "$url?zc=$i"
            val document = Jsoup.connect(currentUrl).cookie("Cookie", cookie).get()
            val elements = document.getElementsByTag("table")
            for (n in 1..<elements.select("tr").size - 1) {
                val time = elements.select("tr").eq(n).select("th").text()
                elements.select("tr").eq(n).select("td").forEachIndexed { date, td ->
                    td.select("div[class=kbcontent]").filterIndexed { _, element -> element.text() != "" }
                        .forEach { div ->
                            val teacher = div.select("font[title=老师]").text()
                            val section = div.select("font[title=周次(节次)]").text()
                            val room = div.select("font[title=教室]").text()
                            courseInfoList.add(CourseInfo(date + 1, teacher, section, room))
                        }
                }
                data.add(Info(time, courseInfoList))
                courseInfoList = mutableListOf()
            }
        }
        data.filter { true }
        return data
    }


}
