<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="D:/Documents/mynt_programming_exam/delivery-parcel/">
    <img src="logo.png" alt="Logo" width="80" height="80">
  </a>

<h3 align="center">Java Programming Exam</h3>
 
</div>

## About The API 
<h3>Objective</h3>
Create a Spring Boot application that provides an API as described below. The application
should be clean, well designed, and maintainable.

Instructions
Create an API that will calculate the cost of delivery of a parcel based on weight and volume
(volume = height * width * length). The API should accept the following:
1. Weight (kg)
2. Height (cm)
3. Width (cm)
4. Length (cm)


   The rules for calculating the cost of delivery are in order of priority:

  <a href="D:/Documents/mynt_programming_exam/delivery-parcel/">
    <img src="table.png" alt="Logo" width="600" height="200">
  </a>


As the market tends to fluctuate in terms of pricing, the rules needs to be as flexible as possible.
Your API should also accept a voucher code that can be used to provide discounts to the
customer. To get the discount details of the submitted voucher code, you will need to integrate
with the voucher service maintained by another team. You may check their Voucher API
definition [here](https://app.swaggerhub.com/apis/mynt-iat/mynt-programming-exams/1.1.0). This also includes the details of the mock server that they have provided for your
testing.


<!-- GETTING STARTED -->
## Getting Started

This is an example of how you may give instructions on setting up your project locally.
To get a local copy up and running follow these simple example steps.

### Prerequisites

This is an example of how to list things you need to use the software and how to install them.
* npm
  ```sh
  npm install npm@latest -g
  ```

### Installation

_Below is an example of how you can instruct your audience on installing and setting up your app. This template doesn't rely on any external dependencies or services._

1. Get a free API Key at [https://example.com](https://example.com)
2. Clone the repo
   ```sh
   git clone https://github.com/your_username_/Project-Name.git
   ```
3. Install NPM packages
   ```sh
   npm install
   ```
4. Enter your API in `config.js`
   ```js
   const API_KEY = 'ENTER YOUR API';
   ```

<!-- CONTACT -->
## Contact

linkedin - [@adrian.baduria](https://www.linkedin.com/in/adrian-baduria/) - adrian.baduria@gmail.com

Project Link: [https://github.com/your_username/repo_name](https://github.com/your_username/repo_name)

<p align="right">(<a href="#readme-top">back to top</a>)</p>
