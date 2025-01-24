package com.example.kitap.service.scrapers;

import com.example.kitap.model.BookPriceModel;
import com.example.kitap.service.scrapers.api.PriceProvider;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class DRService implements PriceProvider {

    /**
     * Fetches the price of a book by its ISBN from D&R website.
     *
     * @param isbn The ISBN of the book.
     * @return A BookPrice object containing site name and price, or null if no price is found.
     */
    @Override
    public BookPriceModel fetchPriceByISBN(String isbn) {
        try {
            // Replace this URL with the actual search URL format for D&R
            String url = "https://www.dr.com.tr/search?q=" + isbn;
            Document document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .header("Accept-Charset", "UTF-8")
                    .get();

            // Check if the document is successfully fetched
            if (document == null) {
                System.err.println("Error fetching page from D&R: Document is null for ISBN " + isbn);
                return null;
            }

            // Attempt to select the price element safely
            Element priceElement = document.selectFirst(".current-price.js-text-current-price");
            if (priceElement == null) {
                System.err.println("Error fetching price from D&R: Price element not found for ISBN " + isbn);
                return null;
            }

            String priceText = priceElement.text();
            if (priceText == null || priceText.isEmpty()) {
                System.err.println("Error fetching price from D&R: Price text is empty for ISBN " + isbn);
                return null;
            }

            double price = PriceComparatorService.parsePrice(priceText);

            // If parsing fails and returns -1, treat it as an error
            if (price == -1) {
                System.err.println("Error parsing price from D&R for ISBN " + isbn + ": " + priceText);
                return null;
            }

            return new BookPriceModel("D&R", price, url);

        } catch (IOException e) {
            System.err.println("IO Error fetching price from D&R for ISBN " + isbn + ": " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Unexpected error fetching price from D&R for ISBN " + isbn + ": " + e.getMessage());
            return null;
        }
    }

}
