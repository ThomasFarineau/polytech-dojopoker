package fr.thomasfar.dojopoker

import java.io.*
import java.util.*
import java.util.stream.Collectors

class Lang {
    private val path: String = Objects.requireNonNull(Thread.currentThread().contextClassLoader.getResource("")).path

    var files: List<File> = Arrays.stream(Objects.requireNonNull(File(path).listFiles()))
        .filter { file: File -> file.name.matches(Regex("[a-z]*_[A-Z]*[.]properties")) }
        .collect(Collectors.toList())

    var currentLocale = Properties()

    val locales: List<String>
        get() = files.stream().map { file: File -> file.name.substring(3, 5) }.collect(Collectors.toList())

    var locale: String
        get() = currentLocale.getProperty("locale.id").substring(3, 5)
        set(locale) {
            for (file in files) if (file.name.matches(Regex("[a-z]*_$locale*[.]properties"))) {
                try {
                    val input: InputStream = FileInputStream(file)
                    currentLocale.load(InputStreamReader(input, "UTF-8"))
                } catch (e: IOException) {
                    println(get("init.locale.select.error"))
                }
            }
        }

    operator fun get(string: String): String {
        return if (currentLocale.getProperty(string) == null) "Lang error : The element $string cannot be found" else currentLocale.getProperty(
            string
        )
    }

    init {
        locale = "EN"
    }
}

