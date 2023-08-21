import bean.CourseInfo
import bean.Info
import kotlinx.coroutines.delay
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

    private var data = mutableListOf<Info>()
    private var courseInfoList: MutableList<CourseInfo> = mutableListOf()

    /**
     * 解析学期每周的课程并返回
     * @return 返回封装好的数据
     */
    fun convert(): MutableList<Info> {
        SslUtils.trustEveryone()
        for (i in 1..3) {
            courseInfoList = mutableListOf()
            val currentUrl = "$url?zc=$i"
            val document = Jsoup.connect(currentUrl).cookie("Cookie", cookie).get()
            val elements = document.getElementsByTag("table")
            for (n in 1..<elements.select("tr").size - 1) {
                val time = elements.select("tr").eq(n).select("th").text()
                elements.select("tr").eq(n).select("td").forEachIndexed { date, td ->
                    td.select("div[class=kbcontent]").forEach { div ->
                        if (div.text() != "") {
                            val teacher = div.select("font[title=老师]").text()
                            val section = div.select("font[title=周次(节次)]").text()
                            val room = div.select("font[title=教室]").text()
                            courseInfoList.add(CourseInfo(date + 1, teacher, section, room))
                        }
                    }
                }
                data.add(Info(time, i, courseInfoList))
                courseInfoList = mutableListOf()
            }
        }
        return data
    }


}
