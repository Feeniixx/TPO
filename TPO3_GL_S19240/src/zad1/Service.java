//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package zad1;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.*;
import java.util.stream.Collectors;

public class Service {
    private static final String KEY = "21bb9c177eff678f8aa3dc3eb4ee2264";
    private String country;
    private Map<String, String> dostepneCurrencies;
    private Request request;

    public Service(String country) {
        this.country = country;
        this.dostepneCurrencies = dostepneCurrencies();
        this.request = new Request();
    }

    public String getWeather(String city) {
        String url= "http://api.openweathermap.org/data/2.5/weather?q=" + city + ","+ this.country+ "&units=metric&appid="
                + KEY;
        Response response = this.request.connection(url);
        return response.Body();
    }


    public static Map<String, String> dostepneCurrencies() {
        Map<String, String> currencies = new HashMap();
        Locale[] variable;
        int variable_3 = (variable = Locale.getAvailableLocales()).length;

        for(int variable_2 = 0; variable_2 < variable_3; ++variable_2) {
            Locale locale = variable[variable_2];
            if (locale != Locale.ROOT) {
                String displayCountry = locale.getDisplayCountry(Locale.ENGLISH);
                if (!displayCountry.equals("")) {
                    currencies.put(displayCountry, Currency.getInstance(locale).getCurrencyCode());
                }
            }
        }

        return currencies;
    }

    public static List<String> dostepneKraje() {
        List<String> countries = new ArrayList();
        Locale[] var4;
        int var3 = (var4 = Locale.getAvailableLocales()).length;

        for(int var2 = 0; var2 < var3; ++var2) {
            Locale locale = var4[var2];
            if (locale != Locale.ROOT) {
                String displayCountry = locale.getDisplayCountry(Locale.ENGLISH);
                if (!displayCountry.equals("")) {
                    countries.add(displayCountry);
                }
            }
        }

        return countries.stream().sorted(String::compareTo).collect(Collectors.toList());
    }

    private String CurrencyKrajow(String country) {
        return this.dostepneCurrencies.get(country);
    }

    public Double getRateFor(String currency) {
        String url = String.format("https://api.exchangerate.host/latest", this.CurrencyKrajow(this.country), currency);
        Response response = this.request.connection(url);
        JsonObject rootObj = (new JsonParser()).parse(response.Body()).getAsJsonObject();
        return rootObj.get("rates").getAsJsonObject().get(currency).getAsDouble();
    }


    public Double getNBPRate() {
        String currencyKrajow = this.CurrencyKrajow(this.country);
        if (currencyKrajow.equalsIgnoreCase("PLN")) {
            return 1.0;
        } else {
            String url = String.format("http://api.nbp.pl/api/exchangerates/rates/a/%s?format=json", currencyKrajow);
            Response response = this.request.connection(url);
            if (response.Status() != 200) {
                url = String.format("http://api.nbp.pl/api/exchangerates/rates/b/%s?format=json", currencyKrajow);
                response = this.request.connection(url);
            }

            if (response.Status() != 200) {
                return null;
            } else {
                JsonObject rootObj = (new JsonParser()).parse(response.Body()).getAsJsonObject();
                return rootObj.get("rates").getAsJsonArray().get(0).getAsJsonObject().get("mid").getAsDouble();
            }
        }
    }
}
