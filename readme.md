# SIRUP Data Crawler

A Java-based data crawler for extracting government institution and work unit data from the Indonesian SIRUP (Sistem Informasi Rencana Umum Pengadaan) system.

## Overview

This application crawls data from the Indonesian government procurement planning system (SIRUP) at `sirup.inaproc.id` and stores it in a MySQL database. It extracts two main types of data:

1. **KLPD (Klasifikasi Lembaga Pemerintah Daerah)** - Government institution classifications
2. **SATKER (Satuan Kerja)** - Work units under each government institution

## Features

- **Automated Data Crawling**: Fetches data from SIRUP API endpoints
- **Database Integration**: Stores data in MySQL using MyBatis ORM
- **Upsert Operations**: Automatically inserts new records or updates existing ones
- **Multi-Institution Support**: Handles different types of government institutions:
  - KEMENTERIAN (Ministries)
  - LEMBAGA (Agencies)
  - PROVINSI (Provinces)
  - KOTA (Cities)
  - KABUPATEN (Regencies)
- **Comprehensive Logging**: Uses Log4j2 for detailed operation logging
- **Error Handling**: Robust error handling for network and database operations
- **JSON Processing**: Parses JSON responses from SIRUP API using Jackson

## Prerequisites

- **Java 21** or higher
- **Maven 3.6+** for dependency management
- **MySQL 8.0+** database server
- **Internet connection** to access SIRUP API endpoints

## Installation

### 1. Clone the Repository
```bash
git clone <repository-url>
cd sirup-data-crawler
```

### 2. Database Setup
Create a MySQL database and configure the connection:

```sql
CREATE DATABASE db_sirup;
```

Create the required tables:

```sql
-- KLPD Table
CREATE TABLE `tbl_klpd` (
    `id_klpd` varchar(5) NOT NULL,
    `nama_klpd` varchar(150) DEFAULT NULL,
    `jenis_klpd` varchar(14) DEFAULT NULL,
    `created_at` timestamp NULL DEFAULT NULL,
    PRIMARY KEY (`id_klpd`)
);

-- SATKER Table
CREATE TABLE `tbl_satker` (
      `id_satker` varchar(10) NOT NULL,
      `nama_satker` varchar(150) DEFAULT NULL,
      `id_klpd` varchar(5) DEFAULT NULL,
      `created_at` timestamp NULL DEFAULT NULL,
      PRIMARY KEY (`id_satker`)
);
```

### 3. Configure Database Connection
Update the database connection settings in `src/main/resources/mybatis-config.xml`:

```xml
<property name="url" value="jdbc:mysql://localhost:3306/db_sirup"/>
<property name="username" value="your_username"/>
<property name="password" value="your_password"/>
```

### 4. Build the Project
```bash
mvn clean compile
```

## Usage

### Running the Application

The application can be run in two modes:

#### 1. KLPD Data Crawling
Uncomment the KLPD generation line in `Main.java`:
```java
public static void main(String[] args) {
    Main main = new Main();
    main.runGenerateTblKlpd(); 
}
```

#### 2. SATKER Data Crawling
Keep the SATKER generation line active (default):
```java
public static void main(String[] args) {
    Main main = new Main();
    main.runGenerateTblSatker(); 
}
```

### Running the Application
```bash
mvn exec:java -Dexec.mainClass="com.edw.Main"
```

### Running Tests
```bash
mvn test
```

Or run the specific test class:
```bash
java -cp target/classes:target/test-classes com.edw.TestSatkerParser
```

## Project Structure

```
sirup-data-crawler/
├── src/
│   ├── main/
│   │   ├── java/com/edw/
│   │   │   ├── Main.java                    # Main application entry point
│   │   │   ├── mapper/
│   │   │   │   ├── TblKlpdMapper.java       # KLPD database operations interface
│   │   │   │   └── TblSatkerMapper.java     # SATKER database operations interface
│   │   │   └── model/
│   │   │       ├── TblKlpd.java             # KLPD data model
│   │   │       └── TblSatker.java           # SATKER data model
│   │   └── resources/
│   │       ├── mapper/
│   │       │   ├── TblKlpdMapper.xml        # KLPD SQL mappings
│   │       │   └── TblSatkerMapper.xml      # SATKER SQL mappings
│   │       ├── mybatis-config.xml           # MyBatis configuration
│   │       └── log4j2.xml                   # Logging configuration
│   └── test/
│       └── java/com/edw/
│           └── TestSatkerParser.java        # JSON parsing test
├── pom.xml                                  # Maven dependencies
└── README.md                                # This file
```

## Dependencies

- **MyBatis 3.5.19** - SQL mapping framework
- **MySQL Connector/J 9.5.0** - MySQL database driver
- **Jackson 2.20.0** - JSON processing library
- **Log4j2 2.20.0** - Logging framework
- **Java HTTP Client** - Built-in HTTP client for API calls

## Configuration

### Logging Configuration
Logging is configured in `src/main/resources/log4j2.xml`. The application logs:
- INFO level: Successful operations and progress updates
- DEBUG level: Detailed parsing information
- ERROR level: Exceptions and failures

### API Endpoints
The application uses these SIRUP API endpoints:
- **KLPD Data**: `https://sirup.inaproc.id/sirup/datatablectr/datatablerupkldi2`
- **SATKER Data**: `https://sirup.inaproc.id/sirup/datatablectr/datatableruprekapkldi`

### Data Processing
- **Year**: Currently set to "2026" (can be modified in the code)
- **Batch Size**: 1000 records per request
- **Institution Types**: KEMENTERIAN, LEMBAGA, PROVINSI, KOTA, KABUPATEN

## Workflow

1. **KLPD Crawling**: 
   - Fetches government institution data for each institution type
   - Stores KLPD records with ID, name, and type
   - Uses upsert logic (insert new, update existing)

2. **SATKER Crawling**:
   - Retrieves all KLPD IDs from the database
   - For each KLPD, fetches associated work units (SATKER)
   - Stores SATKER records with ID, name, and parent KLPD reference

## Error Handling

The application includes comprehensive error handling:
- Network connectivity issues
- JSON parsing errors
- Database connection problems
- Individual record processing failures
- Graceful continuation on partial failures

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/new-feature`)
3. Commit your changes (`git commit -am 'Add new feature'`)
4. Push to the branch (`git push origin feature/new-feature`)
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Author

Muhammad Edwin < edwin at redhat dot com >

## Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Verify MySQL server is running
   - Check database credentials in `mybatis-config.xml`
   - Ensure database `db_sirup` exists

2. **HTTP Request Timeouts**
   - Check internet connectivity
   - Verify SIRUP API endpoints are accessible
   - Consider adding retry logic for network failures

3. **JSON Parsing Errors**
   - API response format may have changed
   - Check the test file for expected JSON structure
   - Enable DEBUG logging to see raw responses

4. **Build Issues**
   - Ensure Java 21 is installed and configured
   - Run `mvn clean install` to refresh dependencies
   - Check Maven configuration and proxy settings

## Support

For issues and questions, please create an issue in the project repository or contact the author.