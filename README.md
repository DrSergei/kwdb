# Key-value база данных

## Работа с утилитой

Представляется консольный и файловый интерфейс, работающие 
в режиме чтения и записи, выбор происходит при запуске программы
через аргументы командной строки.

-i --input [fileName] для ввода из файла(по умолчанию консоль)

-m --mode [READ, WRITE] для выбора режима работы(по умолчанию только чтение)

Дальше происходит работа с пулом баз данных.

Изначально есть пустой пул к которому можно обратиться pool:...

Для загрузки базы данных pool:download:name:dataFile:logFile:key
(имя для обращения к базе потом, файл данных, файл для логов, ключ для расшифровки и шифровки)

Для создания базы данных pool:create:name:dataFile:logFile:key
(имя для обращения к базе потом, файл данных, файл для логов, ключ для расшифровки и шифровки)

Для удаления базы данных из пула name:exit. Будут сохранены все изменения в файл,
база данных будет удалена из пула.

Для просто сохранения name:save. Будут сохранены изменения на диск
можно продолжить работу с базой данных.

Для вставки элемента name:insert:key:value. Вставка value с ключом key
в базу данных с именем name.

Для удаления элемента по ключу name:delete:key. Для оптимизации
элемент будет отмечен как удаленный, но данные сохранятся.

Для удаления элементов по паттерну ключа name:deleteAll:pattern.
Элементы удовлетворяющие данному регулярному выражению будут отмечены удаленными.

Для поиска элемента по ключу name:find:key. Выведет значение, 
если данный ключ существует.

Для поиска элементов по паттерну ключа name:findAll:pattern. 
Выведет все элементы чьи ключи соответствуют данному регулярному выражению.

Для полного удаления элементов name:clear. Удалит все элементы 
из базы данных отмеченные ранее.

Для восстановления элементов name:recovery. Восстановит все
элементы отмеченные ранее как удаленные.

Для получения информации name:size. Выведет число всех записей,
удаленных и активных записей.

Для печати всех пар ключ-значение name:print. Выведет в каждой строке
пару ключ значение через двоеточие.

Во всех предыдущих вместо name можно написать pool и все операции 
будут проделаны для каждой базой данных в пуле. Вывод для каждой будет подписан.

## Подробности реализации

База данных реализована как HashMap и при работе хранится в базе данных.
При загрузке и сохранении сериализуется в json формат, а затем шифруется. 
Для безопасной записи на диск. Используется [шифр Вернама](https://ru.wikipedia.org/wiki/%D0%A8%D0%B8%D1%84%D1%80_%D0%92%D0%B5%D1%80%D0%BD%D0%B0%D0%BC%D0%B0),
для генерации полного ключа по пользовательскому, используется генератор 
псевдослучайных чисел, инициализированный ключом пользователя.

Пул баз данных представляет собой Map с ключом именем и значением базой данных.

Предупреждение. Используются внешние библиотеки 
[kotlinx-serialization-json](https://github.com/Kotlin/kotlinx.serialization),
[kotlinx-cli](https://github.com/Kotlin/kotlinx-cli),
и реализация криптографии [javax-crypto](https://docs.oracle.com/javase/7/docs/api/javax/crypto/package-summary.html).

## Тестирование

Файл с модульными тестами [src/test/kotlin/Test.kt](src/test/kotlin/Test.kt).
При запуске генерируются файлы в [database/test](database/test) при
повторном тестировании надо удалить их. В [database/demo](database/demo)
демонстрационная база данных, не шифрованная(пустой пароль). Для демонстрации работы
с ней используется ввод из файла [database/demo/input.txt](database/demo/input.txt).
Аргументы командной строки для запуска с файлом [-i database/demo/input.txt -m WRITE],
для работы с консолью [-m WRITE].
