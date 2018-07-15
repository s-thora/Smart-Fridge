-- findAvail (dishes) - by Complexity
with groupped as (
SELECT m.id, m.dish_name, m.recipe_content, m.cooking_time, m.price, count(g.category_id)
FROM "Menu" AS m
LEFT JOIN "Recipe groceries" AS rg ON m.id=rg.dish_id
LEFT JOIN "Groceries" as g on rg.grocery_id=g.id
group by m.id, m.dish_name, m.recipe_content, m.cooking_time, m.price, g.category_id
order by m.id
), by_complexity as (
select id, dish_name, recipe_content, cooking_time, price, count(id)
from groupped
group by id, dish_name, recipe_content, cooking_time, price
), inventory as (
SELECT i.id, g.id as grocery_id, g.grocery_name, c.category_name, i.production_date, i.amount, g.minimal_amount, i.added, i.removed, g.keeping_time, g.price, c.measurement_unit, ri.usage_id
FROM "Inventory" AS i
LEFT JOIN "Groceries" AS g ON i.grocery_id=g.id
left JOIN "Registered items" as ri on i.id=ri.inventory_id
LEFT JOIN "Categories" as c ON g.category_id=c.id
where i.removed is null and i.added is not null and ri.inventory_id is not null and ri.type_id=1 and ri.usage_id != 0
order by i.id)
SELECT distinct(by_complexity.id), dish_name, recipe_content, cooking_time, by_complexity.price, by_complexity.count FROM
by_complexity
JOIN "Recipe groceries" AS rg2 ON by_complexity.id=rg2.dish_id
JOIN inventory AS i ON rg2.grocery_id=i.grocery_id
order by by_complexity.count desc;
-------------------------

-- findById item
SELECT i.id, g.id as grocery_id, g.grocery_name, c.category_name, i.production_date, i.amount, g.minimal_amount, i.added, i.removed, g.keeping_time, g.price, c.measurement_unit, ri.usage_id
FROM "Inventory" AS i
LEFT JOIN "Groceries" AS g ON i.grocery_id=g.id
left JOIN "Registered items" as ri on i.id=ri.inventory_id
LEFT JOIN "Categories" as c ON g.category_id=c.id
where i.removed is null and ri.inventory_id is not null and ri.type_id=1
and i.id = ?;

-- findAvail Item
SELECT i.id, g.id as grocery_id, g.grocery_name, c.category_name, i.production_date, i.amount, g.minimal_amount, i.added, i.removed, g.keeping_time, g.price, c.measurement_unit, ri.usage_id
FROM "Inventory" AS i
LEFT JOIN "Groceries" AS g ON i.grocery_id=g.id
left JOIN "Registered items" as ri on i.id=ri.inventory_id
LEFT JOIN "Categories" as c ON g.category_id=c.id
where i.removed is null and i.added is not null and ri.inventory_id is not null and ri.type_id=1 and ri.usage_id != 0
order by i.id

-- findUsedUpItems
SELECT null as id, g.id as grocery_id, g.grocery_name, c.category_name, null as production_date, sum(i.amount) as amount, g.minimal_amount, null as added, null as removed, g.keeping_time, g.price, c.measurement_unit, null as usage_id
FROM "Inventory" AS i
LEFT JOIN "Groceries" AS g ON i.grocery_id=g.id
left JOIN "Registered items" as ri on i.id=ri.inventory_id
LEFT JOIN "Categories" as c ON g.category_id=c.id
where i.removed is null and i.added is not null and ri.inventory_id is not null and ri.type_id=1 and ri.usage_id != 0
group by g.id, c.category_name, c.measurement_unit
having (sum(i.amount) - g.minimal_amount < 0)
ORDER BY g.id;

-- findSpoiledItems
SELECT i.id, g.id as grocery_id, g.grocery_name, c.category_name, i.production_date, i.amount, g.minimal_amount, i.added, i.removed, g.keeping_time, g.price, c.measurement_unit, ri.usage_id
FROM "Groceries" AS g
RIGHT JOIN "Inventory" AS i ON g.id=i.grocery_id
LEFT JOIN "Registered items" as ri on i.grocery_id=ri.grocery_id
LEFT JOIN "Categories" as c ON g.category_id=c.id
where (i.production_date + g.keeping_time < now())
and i.removed is null and i.added is not null and ri.inventory_id is not null and ri.type_id=1 and ri.usage_id != 0
ORDER BY i.grocery_id

-- findMissingItems
SELECT null as id, grocery_id, grocery_name, category_name, null as production_date, MAX(amount) as amount, minimal_amount, null as added, null as removed, keeping_time, price, measurement_unit, null as usage_id FROM
(SELECT id, grocery_id, grocery_name, category_name, production_date, amount, minimal_amount, added, removed, keeping_time, price, measurement_unit, usage_id FROM
(SELECT i.id, g.id as grocery_id, g.grocery_name, c.category_name, i.production_date, i.amount, g.minimal_amount, i.added, i.removed, g.keeping_time, g.price, c.measurement_unit, ri.usage_id
FROM "Groceries" AS g
RIGHT JOIN "Inventory" AS i ON g.id=i.grocery_id
LEFT JOIN "Registered items" as ri on i.grocery_id=ri.grocery_id
LEFT JOIN "Categories" as c ON g.category_id=c.id
where (i.production_date + g.keeping_time < now())
and i.removed is null and i.added is not null and ri.inventory_id is not null and ri.type_id=1 and ri.usage_id != 0
ORDER BY i.grocery_id
) AS spoiled_items
UNION ALL
SELECT null as id, grocery_id, grocery_name, category_name, null as production_date, amount, minimal_amount, null as added, null as removed, keeping_time, price, measurement_unit, null as usage_id FROM
(SELECT null as id, g.id as grocery_id, g.grocery_name, c.category_name, null as production_date, sum(i.amount) as amount, g.minimal_amount, null as added, null as removed, g.keeping_time, g.price, c.measurement_unit, null as usage_id
FROM "Inventory" AS i
LEFT JOIN "Groceries" AS g ON i.grocery_id=g.id
left JOIN "Registered items" as ri on i.id=ri.inventory_id
LEFT JOIN "Categories" as c ON g.category_id=c.id
where i.removed is null and i.added is not null and ri.inventory_id is not null and ri.type_id=1 and ri.usage_id != 0
group by g.id, c.category_name, c.measurement_unit
having (sum(i.amount) - g.minimal_amount < 0)
ORDER BY g.id
) AS used_up_items
) AS lacking_items
GROUP BY lacking_items.grocery_id, lacking_items.grocery_name, lacking_items.category_name, lacking_items.minimal_amount,
lacking_items.keeping_time, lacking_items.price, lacking_items.measurement_unit
ORDER BY grocery_id;

------------------------------

-- findLackingInventoryItems
SELECT ri.inventory_id, i.id, i.grocery_id, g.grocery_name, c.category_name,
i.production_date, i.amount, g.minimal_amount, i.added, i.removed,
g.keeping_time, g.price, c.measurement_unit, ri.usage_id
from "Registered items" as ri
full join "Inventory" as i on ri.inventory_id=i.id
join "Groceries" as g on ri.grocery_id=g.id
join "Categories" as c on g.category_id=c.id
where i.id is null;

-- findExcessingInventoryItems
SELECT ri.inventory_id, i.id, i.grocery_id, g.grocery_name, c.category_name,
i.production_date, i.amount, g.minimal_amount, i.added, i.removed,
g.keeping_time, g.price, c.measurement_unit, ri.usage_id
from "Registered items" as ri
full join "Inventory" as i on ri.inventory_id=i.id
left join "Groceries" as g on i.grocery_id=g.id
join "Categories" as c on g.category_id=c.id
where usage_id is null;

-- findIrregularInventoryItems
select * from (
SELECT ri.inventory_id, i.id, i.grocery_id, g.grocery_name, c.category_name,
i.production_date, i.amount, g.minimal_amount, i.added, i.removed,
g.keeping_time, g.price, c.measurement_unit, ri.usage_id
from "Registered items" as ri
full join "Inventory" as i on ri.inventory_id=i.id
join "Groceries" as g on ri.grocery_id=g.id
join "Categories" as c on g.category_id=c.id
where i.id is null
) as lacking
union all
select * from (
SELECT ri.inventory_id, i.id, i.grocery_id, g.grocery_name, c.category_name,
i.production_date, i.amount, g.minimal_amount, i.added, i.removed,
g.keeping_time, g.price, c.measurement_unit, ri.usage_id
from "Registered items" as ri
full join "Inventory" as i on ri.inventory_id=i.id
left join "Groceries" as g on i.grocery_id=g.id
join "Categories" as c on g.category_id=c.id
where usage_id is null)
as excessing;


--------------------

-- findAllInsertions
SELECT DISTINCT ur.id, ur.usage_timestamp, ut.type_name, ur.user_name
FROM "Usage registration" AS ur
JOIN "Registered items" AS ri ON ur.id=ri.usage_id
JOIN "Usage types" AS ut ON ri.type_id=ut.id
WHERE ri.type_id=1
ORDER BY ur.usage_timestamp;

-- findAllEjections
SELECT DISTINCT ur.id, ur.usage_timestamp, ut.type_name, ur.user_name
FROM "Usage registration" AS ur
JOIN "Registered items" AS ri ON ur.id=ri.usage_id
JOIN "Usage types" AS ut ON ri.type_id=ut.id
WHERE ri.type_id=2
ORDER BY ur.usage_timestamp;

-- findAll (reg us)
SELECT DISTINCT ur.id, ur.usage_timestamp, ut.type_name, ur.user_name
FROM "Usage registration" AS ur
JOIN "Registered items" AS ri ON ur.id=ri.usage_id
JOIN "Usage types" AS ut ON ri.type_id=ut.id
ORDER BY ur.usage_timestamp;

-- findById (reg act)
SELECT DISTINCT ur.id, ur.usage_timestamp, ut.type_name, ur.user_name
FROM "Usage registration" AS ur
JOIN "Registered items" AS ri ON ur.id=ri.usage_id
JOIN "Usage types" AS ut ON ri.type_id=ut.id
WHERE ur.id=?
ORDER BY ur.usage_timestamp;

-----------------------------

-- findAll (groc)
SELECT g.id, g.grocery_name, g.category_id, c.category_name, g.price, g.minimal_amount, g.keeping_time, c.measurement_unit
FROM "Groceries" as g
JOIN "Categories" as c ON g.category_id=c.id;

-- findById (groc)
SELECT g.id, g.grocery_name, g.category_id, c.category_name, g.price, g.minimal_amount, g.keeping_time, c.measurement_unit
FROM "Groceries" as g
JOIN "Categories" as c ON g.category_id=c.id
WHERE g.id=?;

-- findAllByCategoryId (groc)
SELECT g.id, g.grocery_name, g.category_id, c.category_name, g.price, g.minimal_amount, g.keeping_time, c.measurement_unit
FROM "Groceries" as g
JOIN "Categories" as c ON g.category_id=c.id
WHERE g.category_id=?;

-----------------

-- findAll (sl item)
SELECT sl.id, sli.grocery_id, g.grocery_name, c.category_name, sli.amount, g.price, sl.insertion_id, c.measurement_unit
FROM "Shopping lists" AS sl
JOIN "Shopping list items" AS sli ON sl.id=sli.shopping_list_id
JOIN "Groceries" AS g ON sli.grocery_id=g.id
JOIN "Categories" AS c ON g.category_id=c.id;

-- findSLItemByGroceryId (sl item)
SELECT sl.id, sli.grocery_id, g.grocery_name, c.category_name, sli.amount, g.price, sl.insertion_id, c.measurement_unit
FROM "Shopping lists" AS sl
JOIN "Shopping list items" AS sli ON sl.id=sli.shopping_list_id
JOIN "Groceries" AS g ON sli.grocery_id=g.id
JOIN "Categories" AS c ON g.category_id=c.id
WHERE sli.grocery_id = ?;

---
-- stats expenses
-- by month
SELECT  date_part('month', order_date) as month, null as year, SUM(sum) FROM "Orders"
GROUP BY month
ORDER BY month;
-- by year
SELECT null as month, date_part('year', order_date) as year, SUM(sum) FROM "Orders"
GROUP BY year
ORDER BY year;
-- all
SELECT null as month, null as year, SUM(sum) FROM "Orders";

------
-- for expiracy stats
with being_spoiled as (
select date_part('month', i.removed) as month, category_name, count(*)::numeric
from "Inventory" as i
join "Groceries" as g on i.grocery_id=g.id
join "Categories" as c on g.category_id=c.id
join "Registered items" as ri on ri.inventory_id=i.id
join "Usage registration" as ur on ri.usage_id=ur.id
where (cast (concat(g.keeping_time, ' days') as interval) < (i.added - i.removed))
	and i.removed > (now() - interval '12 months')
group by month, c.id
order by month, c.id
),
total as (
select date_part('month', i.removed) as month, category_name, count(*)::numeric
from "Inventory" as i
join "Groceries" as g on i.grocery_id=g.id
join "Categories" as c on g.category_id=c.id
join "Registered items" as ri on ri.inventory_id=i.id
join "Usage registration" as ur on ri.usage_id=ur.id
where i.removed > (now() - interval '12 months')
group by month, c.id
order by month, c.id)
select total.month, total.category_name, being_spoiled.count as exp_count, total.count as ttl_count
from total
full join being_spoiled on total.month=being_spoiled.month and total.category_name=being_spoiled.category_name
order by total.month;

-------
-- reg items (by inventory id)
select * from "Registered items" where inventory_id = ?