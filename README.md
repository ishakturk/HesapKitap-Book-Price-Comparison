# 📚 HesapKitap

## 📝 Description  

HesapKitap is a book price comparison platform that helps users find the best prices for books across multiple e-commerce websites.  

### 🔍 How It Works?  
- Users can search for a book by title,isbn or author.  
- The system scrapes book prices from various e-commerce websites.  
- By default, it retrieves the first five books from **Kitapseç** based on the search query.  
- Prices are displayed in a sorted manner (from lowest to highest).  
- If the user is logged in, they can save books to their **wishlist** for later reference.  

### 🌟 Features  

- **Real-time Price Scraping** – Fetches and displays book prices dynamically.  
- **User Authentication** – Users can register, log in, and manage their accounts.  
- **Admin Panel** – Manage books and users.  
  <div style="display: flex;">
    <img src="https://github.com/user-attachments/assets/4b692325-3e98-4256-aa02-3799d3e0780a" alt="Login" width="400">
    <img src="https://github.com/user-attachments/assets/393cf612-8d02-49a6-a7d6-67e5c9e43db5" alt="Register" width="400">
    <img src="https://github.com/user-attachments/assets/0a0a7685-dbd7-45e1-a501-7388d544c861" alt="Profile" width="400">
    <img src="https://github.com/user-attachments/assets/1720f37f-6e09-4ad4-891a-8089e65aa606" alt="Admin Panel" width="400">
</div>

- **Wishlist** – Save books for future reference.  
  ![wishlist](https://github.com/user-attachments/assets/d43dbcf4-47ff-4a0b-9113-ede72e013c1e)  

- **Book Detail Page** – Displays the latest prices along with a price history chart for the past year.
  <div style="display: flex;">
    <img src="https://github.com/user-attachments/assets/53da68df-03e7-4a20-9952-ce5e42a921e1" alt="book-details-1" width="430">
    <img src="https://github.com/user-attachments/assets/b9702602-8fdf-4f13-b756-f78c26edc04c" alt="book-details-2" width="430">
  </div>

- **Home Page** –  
  - A **search bar** for book searches.  
  - A **trending books section**, displaying the most wishlisted books.  
  - A **random books section** to explore different books.
 ![home](https://github.com/user-attachments/assets/9bcc9029-891c-4f1e-ae8b-47e98c74926c)


- **Search Results Page** –  
  - Shows books related to the search query.  
  - Allows users to add books to their **wishlist** directly from the results page.  
![search-results](https://github.com/user-attachments/assets/d24a1e3e-2e79-4b2f-9d69-15228ebdae2d)

---
## 🛠️ Technologies Used

This project leverages a variety of technologies to deliver a robust and feature-rich experience.  The core technologies employed include:

*   **Spring Boot:** The foundation of the application, providing auto-configuration, dependency injection, and a streamlined development experience.
*   **Spring Web:** Enables the creation of a web application, handling HTTP requests and responses.
*   **Spring Security:** Implements authentication and authorization, ensuring secure access to the application's resources.  Specifically, Spring Security is used for user authentication and password encryption using bcrypt.
*   **Spring Data JPA:** Simplifies database access by reducing the amount of boilerplate code required for common database operations.  It provides an abstraction over JPA (Java Persistence API), making it easier to interact with the database.
*   **Thymeleaf:** A server-side Java template engine used for creating dynamic and interactive front-end views.  Thymeleaf allows for the seamless integration of data with HTML, simplifying front-end development.
*   **MySQL:** A relational database management system used for persistent data storage, including user information, book details, and wishlist items.
*   **Jsoup:** A Java library for parsing HTML.  It is used for web scraping, extracting book prices and other relevant information from various e-commerce websites.
*   **Java Concurrency Utilities:**  The application leverages Java's concurrency utilities (e.g., ExecutorService, Future) for parallel processing of price fetching tasks, resulting in improved performance.

In summary, the project utilizes a modern and effective technology stack, combining the power of Spring Boot for backend development, Spring Data JPA for simplified database interaction, Thymeleaf for the front-end, MySQL for database, and Jsoup for web scraping.  This combination allows for the creation of a scalable, secure, and user-friendly book price comparison platform.

