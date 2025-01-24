package com.example.kitap.service.scrapers;

import com.example.kitap.model.BookPriceModel;
import com.example.kitap.service.scrapers.api.PriceProvider;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


@Service
public class KitapSecService implements PriceProvider {

    private static final String BASE_URL = "https://www.kitapsec.com/Arama/index.php?a=";

    public List<String[]> fetchSearchResults(String searchQuery) {
        List<String[]> books = new ArrayList<>();
        try {
            String encodedQuery = URLEncoder.encode(searchQuery, "Windows-1254");
            String searchUrl = BASE_URL + encodedQuery;

            Document doc = Jsoup.connect(searchUrl)
                    .userAgent("Mozilla/5.0")
                    .header("Accept-Charset", "Windows-1254")
                    .get();

            Elements bookElements = doc.select("div.Ks_UrunSatir");

            for (int i = 0; i < Math.min(5, bookElements.size()); i++) {
                Element bookElement = bookElements.get(i);

                String title = bookElement.select("span[itemprop=name]").text();
                String author = bookElement.select("span[itemprop=author] span[itemprop=name]").text();
                String publisher = bookElement.select("div[itemprop=publisher] span[itemprop=name]").text();
                String price = bookElement.select("font.satis").text();
                String isbn = bookElement.select("meta[itemprop=isbn]").attr("content");

                // Get the specific book URL for this iteration
                String bookUrl = bookElement.select("[itemprop=itemListElement]").attr("id");
                System.out.println("Processing book URL: " + bookUrl);

                String imageUrl = "";
                try {
                    Document bookDocument = Jsoup.connect(bookUrl)
                            .userAgent("Mozilla/5.0")
                            .header("Accept-Charset", "UTF-8")
                            .get();
                    Element linkElement = bookDocument.selectFirst("div.dtyResimlerIc a[itemprop=image]");
                    if (linkElement != null) {
                        imageUrl = linkElement.attr("href");
                    }
                } catch (Exception e) {
                    System.out.println("Error fetching image for book: " + title + " - " + e.getMessage());
                }

                books.add(new String[]{title, author, publisher, price, isbn, imageUrl});
            }

        } catch (Exception e) {
            System.out.println("Error fetching Kitapseç search results: " + e.getMessage());
        }
        return books;
    }

    public BookPriceModel fetchPriceByISBN(String isbn) {
        try {
            // Replace this URL with the actual search URL format for D&R
            String url = "https://www.kitapsec.com/Arama/index.php?a=" + isbn;
            Document document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .header("Accept-Charset", "UTF-8")
                    .get();

            // Extract the price from the HTML structure
            String priceText = document.select(".fiyat .satis").first().text();
            double price = PriceComparatorService.parsePrice(priceText);

            String bookUrl = document.select("div.Ks_UrunSatir[itemprop=itemListElement]").first().attr("id");

            return new BookPriceModel("KitapSeç", price, bookUrl);
        } catch (Exception e) {
            System.err.println("Error fetching price from KitapSeç: " + e.getMessage());
            return null;
        }
    }
}
