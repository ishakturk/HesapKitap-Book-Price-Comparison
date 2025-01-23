package com.example.kitap.service.scrapers;

//
//@Service
//public class AmazonService implements PriceProvider {
//
//
//    /**
//     * Fetches the price of a book by its ISBN from Amazon website.
//     *
//     * @param isbn The ISBN of the book.
//     * @return A BookPrice object containing site name, price, and product link, or null if no price is found.
//     */
//    @Override
//    public BookPrice fetchPriceByISBN(String isbn) {
//        try {
//            // Amazon search URL with ISBN
//            String url = "https://www.amazon.com.tr/s?k=" + isbn;
//            Document searchDocument = Jsoup.connect(url)
//                    .userAgent("Mozilla/5.0")
//                    .header("Accept-Charset", "UTF-8")
//                    .get();
//
//            // Find the first product using the correct container selector
//            Element firstProduct = searchDocument.selectFirst("div.puis-card-container");
//            if (firstProduct == null) {
//                System.err.println("No products found on Amazon for ISBN: " + isbn);
//                return null;
//            }
//
//            // Get the product link using the updated selectors that match the HTML structure
//            Element linkElement = firstProduct.selectFirst("a.a-link-normal.s-no-outline");
//            if (linkElement == null) {
//                // Fallback to alternative link selector if first one fails
//                linkElement = firstProduct.selectFirst("a.a-link-normal.s-underline-text");
//                if (linkElement == null) {
//                    System.err.println("Could not find product link");
//                    return null;
//                }
//            }
//
//            String productLink = "https://www.amazon.com.tr" + linkElement.attr("href");
//
//            // Get the price using the correct price selector
//            Element priceElement = firstProduct.selectFirst("span.a-price span.a-offscreen");
//            if (priceElement == null) {
//                System.err.println("No price found for the product on Amazon.");
//                return null;
//            }
//
//            String priceText = priceElement.text();
//            double price = PriceComparatorService.parsePrice(priceText);
//
//            // Add debug logging
//            System.out.println("Successfully found product. Link: " + productLink + ", Price: " + priceText);
//
//            return new BookPrice("Amazon", price, productLink);
//
//        } catch (Exception e) {
//            System.err.println("Error fetching price from Amazon: " + e.getMessage());
//            e.printStackTrace(); // Add stack trace for better debugging
//            return null;
//        }
//    }
//
//}
