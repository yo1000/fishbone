# {{fishbone}}
Map objects to placeholder in Excel template

## How to use

### pom.xml

```xml
<dependencies>
    <dependency>
        <groupId>com.yo1000</groupId>
        <artifactId>fishbone</artifactId>
        <version>1.0.2</version>
    </dependency>
</dependencies>
```

### template.xslx

|        | A | B | C | D |
|:------:|---|---|---|---|
| **1**  | {{subject}} |   |   |   |
| **2**  | {{date}}    |   |   |   |
| **3**  |   |   |   |   |
| **4**  | {{i.name}} [[i : items : 4,5,V]] | {{i.price}} | {{i.number}} | {{i.subtotal}} |
| **5**  |   |   |   |   |
| **6**  |   |   |   |   |
| **7**  |   |   |   |   |
| **8**  |   |   |   |   |
| **9**  |   |   | Total number | {{totalNumber}} |
| **10** |   |   | Total fee    | {{totalFee}}    |

### Implementation

```java
public static void main(String[] args) {
    Sales sales = new Sales(
            "Flower shop",
            new Date(),
            new ArrayList<SalesStatement>() {
                {
                    add(new SalesStatement("Clover", 450, 1));
                    add(new SalesStatement("Moss phlox", 200, 2));
                    add(new SalesStatement("Rosemary", 270, 1));
                }
            });

    new XlsxSheetMapper().map(
            Application.class.getResourceAsStream("/template.xlsx"),
            sales,
            false,
            new Function1<Workbook, Unit>() {
                @Override
                public Unit invoke(Workbook workbook) {
                    try {
                        workbook.write(new FileOutputStream(new File(
                                "target/template1.wrote.xlsx")));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }
    );
}

public static class SalesStatement {
    private String name;
    private int price;
    private int number;

    public SalesStatement(String name, int price, int number) {
        this.name = name;
        this.price = price;
        this.number = number;
    }

    // Getter/Setter
    
    public int getSubtotal() {
        return getPrice() * getNumber();
    }
}

public static class Sales {
    private String subject;
    private Date date;
    private List<SalesStatement> items;

    public Sales(String subject, Date date, List<SalesStatement> items) {
        this.subject = subject;
        this.date = date;
        this.items = items;
    }

    // Getter/Setter

    public int getTotalNumber() {
        int total = 0;

        for (SalesStatement statement : getItems()) {
            total += statement.getNumber();
        }

        return total;
    }

    public int getTotalFee() {
        int total = 0;

        for (SalesStatement statement : getItems()) {
            total += statement.getSubtotal();
        }

        return total;
    }
}
```

### Result

|        | A | B | C | D |
|:------:|---|---|---|---|
| **1**  | Flower shop |   |   |   |
| **2**  | 2017/05/08  |   |   |   |
| **3**  |   |   |   |   |
| **4**  | Clover     | 450 | 1 | 450 |
| **5**  | Moss phlox | 200 | 2 | 400 |
| **6**  | Rosemary   | 270 | 1 | 270 |
| **7**  |   |   |   |   |
| **8**  |   |   |   |   |
| **9**  |   |   | Total number | 4    |
| **10** |   |   | Total fee    | 1120 |
