import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;

import com.codecool.shop.controller.DBCMethods;
import com.codecool.shop.controller.ProductController;
import com.codecool.shop.dao.*;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.model.*;
import spark.Request;
import spark.Response;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import spark.Session;
import java.util.ArrayList;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {

        // default server settings
        exception(Exception.class, (e, req, res) -> e.printStackTrace());
        staticFileLocation("/public");
        port(8888);
        populateData(); // populate some data for the memory storage
        OrderList cart = new OrderList();
        logger.info("Shopping cart is made. Object name: " + cart);

        //before connection to db and upload, the base files
        before((Request request, Response response) -> {
            DataBaseController dataBaseController = new DBCMethods();
            dataBaseController.makeTables();
            dataBaseController.uploadBaseData();
        });

        // Always start with more specific routes
        get("/hello", (req, res) -> "Hello World");

        // Always add generic routes to the end
        get("/", ProductController::renderProducts, new ThymeleafTemplateEngine());
        // Equivalent with above
        get("/index", (Request req, Response res) -> {

           return new ThymeleafTemplateEngine().render( ProductController.renderProducts(req, res) );
        });

        get("/product", (Request req, Response res) -> {
            cart.orderProcess(1);
            req.session().attribute("cart", cart);
            cart.orderProcess(3);
            req.session().attribute("cart", cart);
            return "session executed";
        });

        get("/cart", (Request req, Response res) -> {
            OrderList cartFromSession = req.session().attribute("cart");
            logger.info("Check the shopping cart.");
            String data = cartFromSession.getInCart().get(1).toString();
            String data2 = cartFromSession.getInCart().get(2).toString();

                return data + "</br>" + data2;
        });

        get("/Id", (request, response) -> {
            String idString = request.params(":id");
            int id = Integer.parseInt(idString);
            cart.orderProcess(id);
            return request.params(":id");
        });

        // Add this line to your project to enable the debug screen
        enableDebugScreen();
    }

    public static void populateData() {

        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        SupplierDao supplierDataStore = SupplierDaoMem.getInstance();

        //setting up a new supplier
        Supplier amazon = new Supplier("Amazon", "Digital content and services");
        supplierDataStore.add(amazon);
        Supplier lenovo = new Supplier("Lenovo", "Computers");
        supplierDataStore.add(lenovo);
        Supplier nokia = new Supplier("Nokia", "Phones");
        supplierDataStore.add(nokia);
        Supplier asus = new Supplier("Asus", "Computers and phones");
        supplierDataStore.add(asus);
        Supplier acer = new Supplier("Acer", "Computers and phones");
        supplierDataStore.add(acer);
        Supplier samsung = new Supplier("Samsung", "Phones and monitors");
        supplierDataStore.add(samsung);
        Supplier philips = new Supplier("Philips", "Monitors");
        supplierDataStore.add(philips);
        Supplier lg = new Supplier("Lg", "Monitors");
        supplierDataStore.add(lg);
        Supplier trust = new Supplier("Trust", "Keyboards");
        supplierDataStore.add(trust);
        Supplier maxtor = new Supplier("Maxtor", "Mass storage");
        supplierDataStore.add(maxtor);
        Supplier hama = new Supplier("Hama", "Mouse");
        supplierDataStore.add(hama);

        //setting up a new product category
        ProductCategory accessories = new ProductCategory("Accessories", "Electronics", "Computer parts and more like mice and keyboards.");
        productCategoryDataStore.add(accessories);
        ProductCategory laptop = new ProductCategory("Laptop", "Electronics", "A laptop, often called a notebook, is a small, portable personal computer with a  form factor, an alphanumeric keyboard on the lower part and a thin LCD or LED computer screen on the upper portion, which is opened up to use the computer.");
        productCategoryDataStore.add(laptop);
        ProductCategory pc = new ProductCategory("Pc", "Electronics", "A personal computer (PC) is a multi-purpose electronic computer whose size, capabilities, and price make it feasible for individual use.");
        productCategoryDataStore.add(pc);
        ProductCategory tablet = new ProductCategory("Tablet", "Electronics", "A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.");
        productCategoryDataStore.add(tablet);
        ProductCategory phone = new ProductCategory("Phone", "Electronics", "A phone is a telecommunications device that permits two or more users to conduct a conversation when they are too far apart to be heard directly.");
        productCategoryDataStore.add(phone);
        ProductCategory monitor = new ProductCategory("Monitor", "Electronics", "A monitor is an electronic visual display for computers.");
        productCategoryDataStore.add(monitor);
        ProductCategory all = new ProductCategory("All", "Electronics", "All products.");
        productCategoryDataStore.add(all);

        //setting up products and printing it
        //Tablet
        productDataStore.add(new Product("Amazon Fire", 49.9f, "USD", "Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.", tablet, amazon));
        productDataStore.add(new Product("Lenovo IdeaPad Miix 700", 479, "USD", "Keyboard cover is included. Fanless Core m5 processor. Full-size USB ports. Adjustable kickstand.", tablet, lenovo));
        productDataStore.add(new Product("Amazon Fire HD 8", 89, "USD", "Amazon's latest Fire HD 8 tablet is a great value for media consumption.", tablet, amazon));
        //Phone
        productDataStore.add(new Product("ASUS ZenFone 3 MAX ZC520TL", 202.46f, "USD", "Dual SIM, 32GB, LTE, Silver", phone, asus));
        productDataStore.add(new Product("Lenovo A2010", 83.57f, "USD", "Dual SIM, 8GB, LTE, Black", phone, lenovo));
        productDataStore.add(new Product("Nokia 3310", 10, "USD", "You can play snake on it, Blue", phone, nokia));
        //PC
        productDataStore.add(new Product("Lenovo IdeaCentre 300-20ISH MT", 400, "USD", "Intel® Core™ i3-6100 3.7GHz, Skylake™, 8GB, 1TB, DVD-RW, nVidia GT730 2GB, Free DOS, Black", pc, lenovo));
        productDataStore.add(new Product("ASUS K31CD-RO021D", 506.68f, "USD", "Intel® Core™ i3-6100 3.70GHz, Skylake™, 4GB, 1TB, nVIDIA GeForce GT 730 2GB, Free DOS, Black", pc, asus));
        productDataStore.add(new Product("Acer M2-601", 384.29f, "USD", "Intel® Core™ i3 6100U 2.30GHz, Skylake™, 8GB, 128 GB SSD, Intel® HD Graphics, Free DOS, Black", pc, acer));
        //Monitor
        productDataStore.add(new Product("Samsung LS24F350FH/00AA LED monitor", 150.01f, "USD", "24\", Full HD, D-Sub, HDMI, Fekete", monitor, samsung));
        productDataStore.add(new Product("Philips 276E6ADSS/00 LED IPS-ADS monitor", 219.95f, "USD", "27\", Full HD, DVI, HDMI, Black", monitor, philips));
        productDataStore.add(new Product("LG 24UD58-B gaming LED monitor", 324.85f, "USD", "24\", Ultra HD, 4K, FreeSync, HDMI, Display port, Black", monitor, lg));
        //Laptop
        productDataStore.add(new Product("Lenovo 110-15IBR laptop", 244.42f, "USD", " Intel® Celeron N3060 1.6GHz, HD, 2GB, 500GB, Intel HD Graphics, Free Dos", laptop, lenovo));
        productDataStore.add(new Product("Asus Transformer Mini T102HA laptop", 506.68f, "USD", "Intel® Atom™ x5-Z8350 1.44 GHz, HD, 4GB DDR3, 128GB SSD, Intel® HD, Microsoft® Windows® 10", laptop, asus));
        productDataStore.add(new Product("Acer Aspire ES1-571-370P laptop", 384.29f, "USD", "ntel® Core™ i3-5005U 2.00 GHz, 4GB DDR3, 1TB, Intel® HD 5500, Linux®", laptop, acer));
        //Accessories
        productDataStore.add(new Product("Trust ClassicLine", 6.96f, "USD", "Hungarian layout", accessories, trust));
        productDataStore.add(new Product("Maxtor M3 portable mass storage", 69.59f, "USD", "1 TB, 2.5\", USB 3.0, Black", accessories, maxtor));
        productDataStore.add(new Product("HAMA Urage Reaper NXT mouse", 24.44f, "USD", "Black with led", accessories, hama));

    }


}
