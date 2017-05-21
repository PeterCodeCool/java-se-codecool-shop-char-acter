package com.codecool.shop.dao.implementation;


import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.ProductCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDaoMem implements ProductCategoryDao {
    private static final Logger logger = LoggerFactory.getLogger(ProductCategoryDao.class);
    private List<ProductCategory> DATA = new ArrayList<>();
    private static ProductCategoryDaoMem instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private ProductCategoryDaoMem() {
    }

    public static ProductCategoryDaoMem getInstance() {
        if (instance == null) {
            instance = new ProductCategoryDaoMem();
        }
        return instance;
    }

    @Override
    public void add(ProductCategory category) {
        category.setId(DATA.size() + 1);
        DATA.add(category);
        logger.info("Created new category: " + category.toString());
    }

    @Override
    public ProductCategory find(int id) {
        return DATA.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void remove(int id) {
        DATA.remove(find(id));
        logger.info("Removed from category: ", find(id));
    }

    @Override
    public List<ProductCategory> getAll() {
        return DATA;
    }
}
