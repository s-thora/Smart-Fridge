package smart_fridge.application.ui.menus;

import smart_fridge.application.rdg.*;
import smart_fridge.application.rdg.finders.GroceryFinder;
import smart_fridge.application.rdg.finders.ItemFinder;
import smart_fridge.application.rdg.finders.MenuItemFinder;
import smart_fridge.application.rdg.finders.ShoppingListFinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Year;

class InputReader extends MainMenu {

    static String readText(BufferedReader br, String kind, int limit) {
        String groceryName;
        while (true) {
            System.out.println("Enter " + kind + ": ");
            try {
                groceryName = br.readLine();
                if (groceryName.length() == 0) {
                    System.out.println(kind + " name value cannot be empty.");
                    continue;
                } else if (groceryName.length() > limit) {
                    System.out.println(kind + " name value is too long. Should be less than " + limit + " symbols.");
                    continue;
                }
                break;
            } catch (IOException e) {
                System.out.println("Wrong input.");
            }
        }
        return groceryName;
    }

    static Integer readCategoryId(BufferedReader br) {
        Integer categoryId = null;
        printCategoriesOnly();
        while (true) {
            System.out.println("Choose the category: ");
            try {
                String input = br.readLine();
                if ("x".equals(input)) break;
                categoryId = Integer.parseInt(input);
                if (categoryId < 0 || categoryId > 12) {
                    System.out.println("Unknown option.");
                    continue;
                }
                break;
            } catch (NumberFormatException | IOException e) {
                System.out.println("Wrong input.");
            }
        }
        return categoryId;
    }

    static Integer readKeepingTime(BufferedReader br) {
        Integer keepingTime = null;
        while (true) {
            try {
                System.out.println("Enter the keeping time: years, from 0 to 20");
                String input = br.readLine();
                if ("x".equals(input)) break;
                Integer yrsKeepingTime = Integer.parseInt(input);
                if (yrsKeepingTime < 0 || yrsKeepingTime > 20) {
                    System.out.println("Years value should be within the required range.");
                    continue;
                }

                System.out.println("Enter the keeping time: months, from 0 to 12");
                input = br.readLine();
                if ("x".equals(input)) break;
                Integer mosKeepingTime = Integer.parseInt(input);
                if (mosKeepingTime < 0 || mosKeepingTime > 12) {
                    System.out.println("Months value should be within the required range.");
                    continue;
                }

                System.out.println("Enter the keeping time: days, from 0 to 31");
                input = br.readLine();
                if ("x".equals(input)) break;
                Integer daysKeepingTime = Integer.parseInt(input);
                if (daysKeepingTime < 0 || daysKeepingTime > 31) {
                    System.out.println("Days value should be within the required range.");
                    continue;
                }

                keepingTime = getDays(yrsKeepingTime, mosKeepingTime, daysKeepingTime);
                break;
            } catch (NumberFormatException | IOException e) {
                System.out.println("Wrong input. Keeping time value should be numeric");
            }
        }
        return keepingTime;
    }

    static String readCookingTime(BufferedReader br) {
        String cookingTime = null;
        while(true) {
            try {
                System.out.println("Enter the cooking time: hours, from 0 to 72");
                String input = br.readLine();
                if ("x".equals(input)) break;
                Integer hrsCookingTime = Integer.parseInt(input);
                if (hrsCookingTime < 0 || hrsCookingTime > 72) {
                    System.out.println("Hours value should be within the required range.");
                    continue;
                }

                System.out.println("Enter the cooking time: minutes, from 0 to 59");
                input = br.readLine();
                if ("x".equals(input)) break;
                Integer mnsCookingTime = Integer.parseInt(input);
                if (mnsCookingTime < 0 || mnsCookingTime > 59) {
                    System.out.println("Minutes value should be within the required range.");
                }

                cookingTime = getInterval(hrsCookingTime, mnsCookingTime);
                break;
            } catch (NumberFormatException | IOException e) {
                System.out.println("Wrong input. Keeping time value should be numeric");
            }
        }
        return cookingTime;
    }

    private static String getInterval(Integer hrs, Integer mns) {
        StringBuilder interval = new StringBuilder();
        if (hrs < 10) interval.append("0").append(hrs);
        else interval.append(hrs);
        if (mns < 10) interval.append(":0").append(mns).append(":00.0");
        else interval.append(":").append(mns).append(":00.0");
        return interval.toString();
    }

    private static Integer getDays(Integer yrs, Integer mos, Integer days) {
        return yrs * 365 + mos * 12 + days * 31;
    }

    static BigDecimal readAmount(BufferedReader br, String kind) {
        BigDecimal minAmount = null;
        while (true) {
            try {
                System.out.println("Enter the " + kind + " amount: ");
                String input = br.readLine();
                if ("x".equals(input)) break;
                minAmount = BigDecimal.valueOf(Integer.parseInt(input));
                if (minAmount.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.println("Amount value should be grater than 0.");
                }
                break;
            } catch (NumberFormatException | IOException e) {
                System.out.println("Wrong input. Amount value should be numeric.");
            }
        }
        return minAmount;
    }

    static BigDecimal readPrice(BufferedReader br) {
        BigDecimal price = null;
        while (true) {
            try {
                System.out.println("Enter the price: ");
                String input = br.readLine();
                if ("x".equals(input)) break;
                price = BigDecimal.valueOf(Integer.parseInt(input));
                if (price.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.println("Price value should be grater than 0.");
                    continue;
                }
                break;
            } catch (NumberFormatException | IOException e) {
                System.out.println("Wrong input. Price value should be numeric.");
            }
        }
        return price;
    }

    static Timestamp readTimestamp(BufferedReader br) {
        Timestamp timestamp = null;
        Date date = null;
        StringBuilder inputDate = new StringBuilder();
        while (true) {
            try {
                System.out.println("Enter the production date: year, from 1970 till current)");
                String input = br.readLine();
                if ("x".equals(input)) break;
                Integer year = Integer.parseInt(input);
                if (year < 1970 || year > Year.now().getValue()) {
                    System.out.println("Years value should be within the required range.");
                    continue;
                }
                inputDate.append(input).append('-');

                System.out.println("Enter the production date: month");
                input = br.readLine();
                if ("x".equals(input)) break;
                Integer month = Integer.parseInt(input);
                if (month < 0 || month > 12) {
                    System.out.println("Chosen month doesn't exist.");
                    continue;
                }
                if (input.length() == 1) inputDate.append('0');
                inputDate.append(input).append('-');

                while(true) {
                    System.out.println("Enter the production date: day");
                    input = br.readLine();
                    if ("x".equals(input)) break;
                    if (input.length() == 1) inputDate.append('0');
                    inputDate.append(input);

                    if ((date = getDate(inputDate.toString())) == null) {
                        System.out.println("Chosen date doesn't exist.");
                        continue;
                    }
                    break;
                }

                if (date != null)
                    timestamp = new Timestamp(date.getTime());
                else
                    return null;
                break;
            } catch (NumberFormatException | IOException e) {
                System.out.println("Wrong input. Keeping time value should be numeric");
            }
        }
        return timestamp;
    }

    private static java.util.Date getDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        return sdf.parse(date, new ParsePosition(0));
    }

    static Grocery getGroceryById(BufferedReader br) {
        Grocery grocery = null;
        while (true) {
            System.out.println("Enter a grocery ID: ");
            try{
                String input = br.readLine();
                if ("x".equals(input)) break;
                int groceryId = Integer.parseInt(input);
                grocery = GroceryFinder.getInstance().findById(groceryId);
                if (grocery == null) {
                    System.out.println("No such grocery exists.");
                    continue;
                }
                break;
            } catch (NumberFormatException | IOException e) {
                System.out.println("Grocery id value should be numeric.");
            } catch (SQLException e) {
                System.out.println("A database error has occurred:" + e.getMessage());
            }
        }
        return grocery;
    }

    static Item getItemById(BufferedReader br) {
        Item item = null;
        while (true) {
            System.out.println("Enter a grocery ID: ");
            try {
                String input = br.readLine();
                if ("x".equals(input)) break;
                int itemId = Integer.parseInt(input);
                item = ItemFinder.getInstance().findById(itemId);
                if (item == null) {
                    System.out.println("No such item exists.");
                    continue;
                }
                break;
            } catch (NumberFormatException | IOException e) {
                System.out.println("Grocery id value should be numeric.");
            } catch (SQLException e) {
                System.out.println("A database error has occurred:" + e.getMessage());
            }
        }
        return item;
    }

    static MenuItem getMenuItemById(BufferedReader br) {
        MenuItem menuItem = null;
        while (true) {
            System.out.println("Enter a menu item ID: ");
            try{
                String input = br.readLine();
                if ("x".equals(input)) break;
                int menuIemID = Integer.parseInt(input);
                menuItem = MenuItemFinder.getInstance().findById(menuIemID);
                break;
            } catch (NumberFormatException | IOException e) {
                System.out.println("Menu item id value should be numeric.");
            } catch (SQLException e) {
                System.out.println("A database error has occurred:" + e.getMessage());
            }
        }
        return menuItem;
    }

    static ShoppingList getSLById(BufferedReader br) {
        ShoppingList shoppingList = null;
        while (true) {
            System.out.println("Enter a shopping list ID: ");
            try{
                String input = br.readLine();
                if ("x".equals(input)) break;
                int slId = Integer.parseInt(input);
                shoppingList = ShoppingListFinder.getInstance().findById(slId);
                break;
            } catch (NumberFormatException | IOException e) {
                System.out.println("Shopping list id value should be numeric.");
            } catch (SQLException e) {
                System.out.println("A database error has occurred:" + e.getMessage());
            }
        }
        return shoppingList;
    }

    private static void printCategoriesOnly() {
        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
        System.out.println("◆ 1.    Grain products                 ◆");
        System.out.println("◆ 2.    Seasonings                     ◆");
        System.out.println("◆ 3.    Oils                           ◆");
        System.out.println("◆ 4.    Meat                           ◆");
        System.out.println("◆ 5.    Vegetables                     ◆");
        System.out.println("◆ 6.    Plants                         ◆");
        System.out.println("◆ 7.    Soy products                   ◆");
        System.out.println("◆ 8.    Plant milk                     ◆");
        System.out.println("◆ 9.    Seafood                        ◆");
        System.out.println("◆ 10.   Chilli pepper pastes           ◆");
        System.out.println("◆ 11.   Beverage                       ◆");
        System.out.println("◆ 12.   Alcohol                        ◆");
        System.out.println("◆ x.    Back                           ◆");
        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
    }
}
