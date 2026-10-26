# LinkedIn UI Test Automation (Selenium + TestNG)

Automated UI test suite for **LinkedIn**, written for the module **IT3153 - Software Quality Assurance** (General Sir John Kotelawala Defence University).

The suite automates three core user flows:

1. **Login** (valid and invalid password)
2. **Search** (global search box)
3. **Open a search result** (click the first result and verify the opened page)

## Tools and versions

| Item | Version / Choice |
|---|---|
| Language | Java 17 or later |
| Build tool | Apache Maven |
| Automation library | Selenium WebDriver 4.25.0 |
| Test framework | TestNG 7.10.2 |
| Browser | Google Chrome (ChromeDriver is downloaded automatically by Selenium Manager) |
| IDE | Visual Studio Code (Extension Pack for Java) |
| Design pattern | Page Object Model (POM) with explicit waits (`WebDriverWait`) |

## Prerequisites

- JDK 17 or later (`java -version`)
- Apache Maven (`mvn -v`)
- Google Chrome (latest version)
- A **dedicated LinkedIn test account** (do not use someone else's account)

## Ethics and safety

LinkedIn's User Agreement restricts automated access, and the site uses bot detection.

- Use **only your own account**, preferably a dedicated test account.
- If LinkedIn shows a **CAPTCHA or verification step, do not bypass it.** Pause the test and complete the step manually in the browser window. The home page wait allows up to 120 seconds for this.
- Do not run the suite many times in a row. Repeated logins can cause LinkedIn to restrict the account.

## Configure credentials

Credentials are **never committed to Git**. `src/test/resources/config.properties` is listed in `.gitignore`.

### Option 1: config file

Create or edit `src/test/resources/config.properties`:

```properties
linkedin.username=your_test_account_email
linkedin.password=your_test_account_password
search.term=virtusa
```

Notes:

- Use plain `key=value` lines only, with no spaces after the value.
- Avoid backslashes (`\`) in the password, since `.properties` files treat them as escape characters.
- `search.term` can be a person, a company or a topic (for example `software testing`).

### Option 2: environment variables (override the file)

PowerShell (use single quotes so special characters are kept):

```powershell
$env:LINKEDIN_USERNAME='your_test_account_email'
$env:LINKEDIN_PASSWORD='your_test_account_password'
```

Command Prompt / Linux / macOS:

```bash
export LINKEDIN_USERNAME="your_test_account_email"
export LINKEDIN_PASSWORD="your_test_account_password"
```

## How to run the tests

Open a terminal in the project root (the folder containing `pom.xml`).

### Run the full suite

```powershell
mvn clean test
```

This runs all four tests and logs in four times, so avoid running it repeatedly.

### Run a smaller suite

On PowerShell, keep the `-D` argument **in double quotes**.

```powershell
# Login tests only
mvn clean test "-Dsurefire.suiteXmlFiles=testng-login.xml"

# Search tests (search + open result)
mvn clean test "-Dsurefire.suiteXmlFiles=testng-search.xml"

# Open-result test only
mvn clean test "-Dsurefire.suiteXmlFiles=testng-result.xml"
```

You can also run `testng.xml` from the VS Code **Testing** panel.

### View results

- Console output shows `Tests run: ..., Failures: ...`
- Reports are written to `target/surefire-reports/`
- Screenshots are written to `screenshots/`
- Debug dumps (HTML and element lists, written when a locator is not found) are written to `debug/`

## Test cases

| ID | Test | Description | Expected result |
|---|---|---|---|
| TC01 | `LoginTest.validLogin` | Open login page, enter valid credentials, click Sign in | Feed URL (`/feed`) and the search box are displayed |
| TC02 | `LoginTest.invalidLogin` | Log in with a wrong password | An error message is displayed |
| TC03 | `SearchTest.searchShowsResults` | Log in, type the search term, press Enter | URL contains `/search/results/` and at least one result is visible |
| TC04 | `SearchResultTest.openFirstResult` | Search, click the first result | Opened URL contains the clicked link's path and a page heading is visible |

## Project structure

```
linkedin_ui_tests/
├── pom.xml
├── testng.xml                 # full suite
├── testng-login.xml           # login tests only
├── testng-search.xml          # search tests
├── testng-result.xml          # open-result test only
├── .gitignore
├── README.md
├── screenshots/               # generated at run time
├── debug/                     # generated at run time
└── src/test/
    ├── java/
    │   ├── base/
    │   │   └── BaseTest.java          # starts/closes Chrome for each test
    │   ├── pages/
    │   │   ├── LoginPage.java
    │   │   ├── HomePage.java
    │   │   └── SearchResultsPage.java
    │   ├── tests/
    │   │   ├── LoginTest.java
    │   │   ├── SearchTest.java
    │   │   └── SearchResultTest.java
    │   └── utils/
    │       ├── ConfigReader.java      # reads config file / env variables
    │       ├── WaitUtils.java         # waits for the first DISPLAYED element
    │       ├── ScreenshotUtils.java   # saves screenshots
    │       ├── DebugUtils.java        # dumps page info when a locator fails
    │       └── TestListener.java      # screenshot on pass and fail
    └── resources/
        └── config.properties          # NOT committed (gitignored)
```

## Design notes

- **Page Object Model:** locators and page actions live in `pages/`. Tests contain only steps and assertions.
- **Waits:** explicit waits (`WebDriverWait` with `ExpectedConditions` or a custom condition) are used for all synchronisation. Implicit waits are not used.
- **Locators:** LinkedIn generates ids and class names, and its login page contains duplicate hidden fields. The suite therefore uses stable attributes (`type`, `autocomplete`, `data-testid`, `placeholder`) and always selects the first **displayed** match. The login error is detected by its text.
- **Screenshots:** `TestListener` captures a screenshot automatically after every passed or failed test, and tests also capture key steps (login page, home page, search results, opened result).

## Screenshots

Screenshots are saved to `screenshots/` with a prefix and a timestamp, for example:

## Known limitations

- LinkedIn changes its page structure often, so locators may need updating.
- CAPTCHAs and security checkpoints cannot be automated and must be solved manually.
- Bot detection may temporarily restrict the account, so tests should be run sparingly.
- The first search result type (person, company or other) can vary, so the test verifies the opened URL against the link that was actually clicked.
- Chrome may print `Unable to find CDP implementation` warnings. They are harmless.

## Troubleshooting

| Problem | Fix |
|---|---|
| `Unknown lifecycle phase ".suiteXmlFiles=..."` | In PowerShell, put the `-D...` argument in double quotes |
| Timeout waiting for the login field | LinkedIn may have changed its layout or shown a verification page. Check the newest file in `screenshots/` and the `=== DEBUG DUMP ===` block in the console |
| Credentials not found | Check `src/test/resources/config.properties` or the environment variables |
| Account restricted or CAPTCHA appears often | Stop running tests for a while and use fewer runs |