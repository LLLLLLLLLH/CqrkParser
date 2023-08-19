fun main() {

    val convert = Parser(
        "https://jwxt-18080.webvpn.cqrk.edu.cn:8480/jsxsd/xskb/xskb_list.do",
        "Path=/; JSESSIONID=66C8F0D1B75CF468795686F2B9FCB53A; _webvpn_key=eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjoiUlcyMDUwMDEyOTY0MTY1NCIsImdyb3VwcyI6WzEsNV0sImlhdCI6MTY5MjQyOTM0NiwiZXhwIjoxNjkyNTE1NzQ2fQ.3a_xiGd9Xx6UfBSrcjZOl0kkZ_ThLHeDn0Z13hcQQnA; webvpn_username=RW20500129641654%7C1692429346%7C2ae80adf010058dc6e7127858c5ef65f0d05d99f; name=value; Path=/; JSESSIONID=B8CC5EE65586133F04ADE917BF6DC4A6"
    ).convert()
    convert.forEach { it ->
        println(it.time)
        it.data.forEach {
            println(it.week)
            println(it.teacher)
            println(it.section)
            println(it.room)
        }
    }
}