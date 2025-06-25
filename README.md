
## 🚌 Bus Count Automation

### 📌 Overview
A Java-based automation tool that uses Selenium to scrape bus route data from [redbus.in](https://www.redbus.in). It extracts, processes, and saves data in CSV format for further analysis.

### 🚀 Features
- Automated browser control via Selenium WebDriver  
- Input-driven scraping with CSV support  
- Exports clean output data to `output.csv`

### 🧰 Requirements
- Java (JDK 8 or later)  
- Selenium WebDriver  
- ChromeDriver (Ensure `chromedriver.exe` path is correct)

### 🔧 Setup
1. Clone this repo:
   ```bash
   git clone https://github.com/your-username/bus-count-automation.git
   ```
2. Update file paths and dependencies in `BusCountAutomation.java`.  
3. Run the script from your IDE or terminal.
   

### 📁 Files
- `BusCountAutomation.java`: Main automation logic  
- `input.csv`: Sample input  
- `output.csv`: Results of scraping

### 💡 To Do
- Add error handling for timeouts  
- Support dynamic city input from CLI  
- Export to JSON format

### 📄 License
This project is open source under the MIT License.  
