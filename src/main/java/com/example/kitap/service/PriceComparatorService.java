package com.example.kitap.service;

import com.example.kitap.model.BookDetails;
import com.example.kitap.model.BookPrice;
import com.example.kitap.service.api.PriceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


@Service
public class PriceComparatorService {

    private final List<PriceProvider> priceProviders;

    @Autowired
    public PriceComparatorService(List<PriceProvider> priceProviders) {
        this.priceProviders = priceProviders;
    }

    public List<BookDetails> fetchAndSortPricesBySearchQuery(String searchQuery) {
        List<BookDetails> bookDetailsList = new ArrayList<>();

        // KitapSeç sonuçlarından ISBN'leri al
        List<String[]> searchResults = ((KitapSecService) priceProviders.stream()
                .filter(provider -> provider instanceof KitapSecService)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("KitapSecService bulunamadı.")))
                .fetchSearchResults(searchQuery);

        // Thread pool for parallel processing
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<Future<BookDetails>> futures = new ArrayList<>();

        // Her bir ISBN için paralel işlemler
        for (String[] result : searchResults) {
            String title = result[0];
            String author = result[1];
            String publisher = result[2];
            String isbn = result[4];
            String imageUrl = result[5];

            // Her kitap için ayrı bir görev oluştur
            Future<BookDetails> future = executor.submit(() -> {
                List<BookPrice> prices = fetchPricesFromAllSites(isbn);
                return new BookDetails(title, author, publisher, prices, imageUrl);
            });

            futures.add(future);
        }

        // Görevlerin tamamlanmasını bekle ve sonuçları topla
        for (Future<BookDetails> future : futures) {
            try {
                BookDetails bookDetails = future.get();  // Blocking call, waits for result
                if (bookDetails != null) {
                    bookDetailsList.add(bookDetails);
                }
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Error fetching book details: " + e.getMessage());
            }
        }

        executor.shutdown();

        return bookDetailsList;
    }

    private List<BookPrice> fetchPricesFromAllSites(String isbn) {
        List<BookPrice> allPrices = new ArrayList<>();

        // Thread pool for parallel price fetching
        ExecutorService executor = Executors.newFixedThreadPool(priceProviders.size());
        List<Future<BookPrice>> futures = new ArrayList<>();

        // Tüm sağlayıcılar için paralel görevler
        for (PriceProvider provider : priceProviders) {
            Future<BookPrice> future = executor.submit(() -> provider.fetchPriceByISBN(isbn));
            futures.add(future);
        }

        // Görevlerin sonuçlarını topla
        for (Future<BookPrice> future : futures) {
            try {
                BookPrice price = future.get();
                if (price != null) {
                    allPrices.add(price);
                }
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Error fetching price: " + e.getMessage());
            }
        }

        executor.shutdown();

        // Fiyatları küçükten büyüğe sırala
        return allPrices.stream()
                .sorted(Comparator.comparingDouble(BookPrice::getPrice))
                .toList();
    }

    public static double parsePrice(String priceText) {
        try {
            String cleanPrice = priceText.trim()
                    .replaceAll("[^\\d.,]", "");

            // Virgül varsa ve sondaysa, ondalık ayracı olarak kabul et
            if (cleanPrice.contains(",") && cleanPrice.lastIndexOf(",") > cleanPrice.lastIndexOf(".")) {
                cleanPrice = cleanPrice.replace(".", "").replace(",", ".");
            }

            return Double.parseDouble(cleanPrice);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing price: " + priceText);
            return -1;
        }
    }
}
