package rip.crystal.practice.category;

import com.google.common.collect.Maps;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.profile.Profile;
import rip.crystal.practice.profile.file.impl.FlatFileIProfile;
import rip.crystal.practice.profile.file.impl.MongoDBIProfile;
import rip.crystal.practice.utilities.InventoryUtil;
import rip.crystal.practice.utilities.TaskUtil;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import org.bson.Document;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class Category {

    private static ConfigurationSection config;
    @Getter private static final Map<String, Category> categories = Maps.newHashMap();
    public static MongoCollection<Document> collection;

    private final String name;
    private String prefix;
    private int requiredPoints;
    private ItemStack[] armor;

    public Category(String name){
        this.name = name;
        this.requiredPoints = 0;
        this.prefix = ChatColor.GOLD + name;
    }

    public static void init() {
        if (Profile.getIProfile() instanceof MongoDBIProfile) {
            collection = cPractice.get().getMongoDatabase().getCollection("categories");
            collection.find().forEach((Block<? super Document>) document -> {
                String name = document.getString("name");
                int requiredPoints = document.getInteger("requiredPoints");
                String prefix = document.getString("prefix");
                ItemStack[] armor = null;
                if (document.getString("armor") != null) {
                    try {
                        armor = InventoryUtil.itemStackArrayFromBase64(document.getString("armor"));
                    } catch (IOException ignore) {
                        System.out.print("CATEGORY DOESN'T CREATED");
                        return;
                    }
                }
                categories.put(name, new Category(name, prefix, requiredPoints, armor));
            });
        }
        else if (Profile.getIProfile() instanceof FlatFileIProfile) {
            config = cPractice.get().getCategoriesConfig().getConfiguration().getConfigurationSection("categories");
            config.getKeys(false).forEach(s -> {
                ConfigurationSection section = config.getConfigurationSection(s);
                int requiredPoints = section.getInt("requiredPoints");
                String prefix = section.getString("prefix");
                ItemStack[] armor = null;
                if (section.getString("armor") != null) {
                    try {
                        armor = InventoryUtil.itemStackArrayFromBase64(section.getString("armor"));
                    } catch (IOException ignore) {
                        System.out.print("CATEGORY DOESN'T CREATED");
                        return;
                    }
                }
                categories.put(s, new Category(s, prefix, requiredPoints, armor));
            });
        }
    }

    public void save() {
        TaskUtil.runAsync(() -> {
            if (Profile.getIProfile() instanceof MongoDBIProfile) {
                Document document = new Document();
                document.put("name", name);
                document.put("requiredPoints", requiredPoints);
                document.put("prefix", prefix);
                if (armor != null) {
                    document.put("armor", InventoryUtil.itemStackArrayToBase64(armor));
                }
                collection.replaceOne(Filters.eq("name", name), document, new ReplaceOptions().upsert(true));
            }
            else if (Profile.getIProfile() instanceof FlatFileIProfile) {
                ConfigurationSection section;
                if (config.getConfigurationSection(name) != null) section = config.getConfigurationSection(name);
                else section = config.createSection(name);

                section.set("requiredPoints", requiredPoints);
                section.set("prefix", prefix);
                if (armor != null) {
                    section.set("armor", InventoryUtil.itemStackArrayToBase64(armor));
                }

                cPractice.get().getCategoriesConfig().save();
                cPractice.get().getCategoriesConfig().reload();
            }
        });
    }

    public static Category getCategory(String name){
        if (categories.get(name) == null) return new Category("Default");
        return categories.get(name);
    }

//    public static Category getDefault(){
//        if (getCategories().isEmpty()) return new Category("BronzeIV");
//        else return getCategory("BronzeIV");
//    }

    public void delete() {
        TaskUtil.runAsync(() -> {
            Category.getCategories().remove(getName());

            if (Profile.getIProfile() instanceof MongoDBIProfile) collection.deleteOne(Filters.eq("name", name));
            else if (Profile.getIProfile() instanceof FlatFileIProfile) {
                config.set("categories." + name, null);
                cPractice.get().getCategoriesConfig().save();
                cPractice.get().getCategoriesConfig().reload();
            }
        });
    }
}
