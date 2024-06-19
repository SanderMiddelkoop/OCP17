package OCPEssentials;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class C11Localization {
    public static void main(String... args) {
        new LocaleLesson().locales();
        new ResourceBundleLesson().resourceBundles();
    }
}

class LocaleLesson {
    public void locales() {
        //Localization is the process of supporting multiple locales/geographic regions in your input/output
        pickingAlocale();
        localizingNumbers();
        localizingDates();
    }

    private void pickingAlocale() {
        //A locale is defined as a specific geographical location
        //In practice it is a language_country combination
        //View the default locale by doing:
        System.out.println(Locale.getDefault());
        //You can also change it via setDefault(Locale locale)
        //The format of a locale is [language]_[country] -- en_US for example

        //To get a locale, you can use built in locales:
        System.out.println(Locale.GERMAN); //:: de -- notice how the country code DE is not obligatory
        System.out.println(Locale.GERMANY); //:: de_DE -- the language code de IS obligatory

        //You can also put in your own locale:
        System.out.println(new Locale("nl")); //:: nl
        System.out.println(new Locale("nl","NL")); //:: nl_NL

        //Finally, you can use a builder
        System.out.println(new Locale.Builder().setLanguage("nl").build()); //:: nl
        System.out.println(new Locale.Builder().setLanguage("nl").setRegion("NL").build()); //:: nl_NL
        System.out.println(new Locale.Builder().setRegion("NL").build()); //:: _NL -> incomplete
    }

    private void localizingNumbers() {
        double number = 12.67;
        //The format of numbers can change with locales, numberformat object can take a locale as input
        //Get a numberformat instance with default locale:
        NumberFormat decimalUS = NumberFormat.getInstance(Locale.US);
        //Get a numberformat with German locale:
        NumberFormat decimalGE = NumberFormat.getInstance(Locale.GERMANY);
        System.out.println(decimalUS.format(number)); //:: 12,67
        System.out.println(decimalGE.format(number)); //:: 12,67

        //Currency formatters
        NumberFormat currencyUS = NumberFormat.getCurrencyInstance(Locale.US);
        NumberFormat currencyGE = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        System.out.println(currencyUS.format(number)); //:: $12,67
        System.out.println(currencyGE.format(number)); //:: 12,67 €

        //Percentage formatters
        double percent = 12.81;
        NumberFormat percentageUS = NumberFormat.getPercentInstance(Locale.US);
        NumberFormat percentageGE = NumberFormat.getPercentInstance(Locale.GERMANY);
        System.out.println(percentageUS.format(percent)); //:: 81%
        System.out.println(percentageGE.format(percent)); //:: 81 %

        //Parsing a number also takes an instance:
        try {
            System.out.println(decimalUS.parse("12.67")); //12.67
            System.out.println(decimalUS.parse("12,67")); //1267
            System.out.println(decimalGE.parse("12,67")); //12.67
            System.out.println(decimalGE.parse("12.67")); //1267
            //Similarly you can parse a currency:
            System.out.println(currencyUS.parse("$92")); //:: 92
            System.out.println(currencyGE.parse("12,67 €"));// ::12.67
        } catch (Exception e){     };

        //The final formatter you need to know about is the compactNumberFormat
        NumberFormat compactUSShort = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
        NumberFormat compactUSLong = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.LONG);
        NumberFormat compactGEShort = NumberFormat.getCompactNumberInstance(Locale.GERMANY, NumberFormat.Style.SHORT);
        NumberFormat compactGELong = NumberFormat.getCompactNumberInstance(Locale.GERMANY, NumberFormat.Style.LONG);

        long numberLong = 1_234_000_123L;
        System.out.println(compactUSShort.format(numberLong)); //:: 1B
        System.out.println(compactUSLong.format(numberLong)); //:: 1 billion
        System.out.println(compactGEShort.format(numberLong)); //:: 1 Mrd
        System.out.println(compactGELong.format(numberLong)); //:: 1 Milliarde
        //The compact number format will print at most 3 numbers, followed by a description of the unit
    }

    private void localizingDates() {
        //You can use a factory DateTimeFormatter to use the default locale:
        DateTimeFormatter dateFUS = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(Locale.US);
        DateTimeFormatter timeFUS = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.US);
        DateTimeFormatter dateTimeFUS = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT).withLocale(Locale.US);

        //You can also use a locale
        DateTimeFormatter dateFGER = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(Locale.GERMANY);
        DateTimeFormatter timeFGER = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.GERMANY);
        DateTimeFormatter dateTimeFGER = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT).withLocale(Locale.GERMANY);

        LocalDateTime ldt = LocalDateTime.of(2024, 12, 6, 15, 24, 3);
        System.out.println(dateFUS.format(ldt)); //:: 12/6/24
        System.out.println(dateFGER.format(ldt)); //:: 06.12.04
        System.out.println(timeFUS.format(ldt)); //:: 3:24 PM
        System.out.println(timeFGER.format(ldt)); //:: 15:24
        System.out.println(dateTimeFUS.format(ldt));
        System.out.println(dateTimeFGER.format(ldt));

        //Finally, you can seperate the locale used for formatting from the locale used for displaying:
        Locale.setDefault(Locale.Category.DISPLAY, Locale.US); //Locale used for displaying data about locale
        Locale.setDefault(Locale.Category.FORMAT, Locale.US); //Locale used for any display/ui purposes

    }
}

class ResourceBundleLesson {
    public void resourceBundles() {
        //A resource bundle contains locale specific objects
        //It is commonly stored in a properties file
        creatingAResourceBundle();
        pickingAResourceBundle();
        selectingBundleValues();
        formattingAndProperties();
    }

    private void creatingAResourceBundle() {
        //You can create a resource bundle by making a xx_[locale].properties file
        //You can then access the bundle by typing:
        var bundleUS = ResourceBundle.getBundle("Resource",new Locale("en","US"));
        var bundleGE = ResourceBundle.getBundle("Resource",new Locale("de","DE"));
        //or get the default locale bundle:
        var bundleDEFAULT = ResourceBundle.getBundle("Resource");

        System.out.println(bundleUS.getString("hello")); //:: hello
        System.out.println(bundleGE.getString("hello")); //:: hallo
        System.out.println(bundleDEFAULT.getString("hello")); //:: hello

    }

    private void pickingAResourceBundle() {
        //Java tries to look for a resource bundle in the following order:
        //When you getBundle("X",new Locale("en", "US"):
        //  1) Look for a X_en_US.properties file
        //  2) Look for a X_en.properties file
        //  3) Look for X_[defaultLang]_[defaultCountry].properties file
        //  4) Look for X_[defaultLang].properties file
        //  5) Look for X.properties
    }

    private void selectingBundleValues() {
        //Resource bundles "inherit" values from resource bundles that include them
        //For example, Resource_de_DE inherits from Resource_de, because Resource_de is broader
        //Resource_de in term, inherits from Resource.properties
        //This means that:

        var bundleGE = ResourceBundle.getBundle("Resource",new Locale("de","DE"));
        //Using the Resource_de.properties bundle

        System.out.println(bundleGE.getString("hello")); //:: Hallo -- found hey hello in the Resource_de bundle
        System.out.println(bundleGE.getString("inherited")); //:: inherited -- there's no key inherited in the
        //                                                          Resource_de bundle, but it is inherited via the
        //                                                          Resource.properties bundle
    }

    private void formattingAndProperties() {
        //You can use MessageFormat to easily input parameters in complex strings:
        String format = "Input parameter 1: {0} ;\nInput parameter 2: {1} !";
        System.out.println(MessageFormat.format(format, 12, "23")); //:: Input parameter 1: 12 ;
        //                                                                       :: Input parameter 2: 23 !
        //format uses ToString() of objects you put in

        //A resource bundle represents a properties class, this is basically a HashMap with string,string pairs
        Properties properties = new Properties();
        //Set a property
        properties.setProperty("name","Tim");
        //Get a property
        System.out.println(properties.getProperty("name")); //:: Tim
        System.out.println(properties.get("name")); //:: Tim

        //The getProperty method allows a default:
        System.out.println(properties.getProperty("lastname","none")); //:: none
    }
}
