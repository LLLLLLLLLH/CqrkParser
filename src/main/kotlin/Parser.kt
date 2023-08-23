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

    private var data = mutableListOf<Info>()
    private var courseInfoList: MutableList<CourseInfo> = mutableListOf()

    /**
     * 解析学期每周的课程并返回
     * @return 返回封装好的数据
     */
    fun convert(): MutableList<Info> {
        SslUtils.trustEveryone()
        for (i in 1..17) {
            courseInfoList = mutableListOf()
            val currentUrl = "$url?zc=$i"
            val document = Jsoup.connect(currentUrl).cookie("Cookie", cookie).get()
            val elements = document.getElementsByTag("table")
            for (n in 1..<elements.select("tr").size - 1) {
                val time = elements.select("tr").eq(n).select("th").text()
                elements.select("tr").eq(n).select("td").forEachIndexed { date, td ->
                    td.select("div[class=kbcontent]").forEach { div ->
                        if (div.text() != "") {
                            val teacher = handleTeacher(div.select("font[title=老师]").text())
                            val section = handleSection(div.select("font[title=周次(节次)]").text())
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

    private fun handleSection(section: String): String {
        val begin = section.indexOf("[") + 1
        val end = section.indexOf("]")
        return section.substring(begin, end)
    }

    private fun handleTeacher(teacher: String) = when {
        teacher.contains("无") -> teacher.substring(0, teacher.indexOf("无"))
        teacher.contains("副高") -> teacher.substring(0, teacher.indexOf("副高"))
        teacher.contains("中级") -> teacher.substring(0, teacher.indexOf("中级"))
        teacher.contains("未评级") -> teacher.substring(0, teacher.indexOf("未评级"))
        teacher.contains("初级") -> teacher.substring(0, teacher.indexOf("初级"))
        teacher.contains("高级") -> teacher.substring(0, teacher.indexOf("高级"))
        else -> teacher // 如果都不符合条件，则返回原字符串
    }


}
