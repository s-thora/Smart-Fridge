/*
 *inspired by shanki
 */

truncate table
    "Categories",
    "Groceries",
    "Inventory",
    "Menu",
    "Shopping lists",
    "Orders",
    "Recipe groceries",
    "Registered items",
    "Usage registration",
    "Usage types"
restart identity cascade;

---- main tables

insert into "Categories" (measurement_unit, category_name)
select	random_unit(),
        category_name
from category_names;

-------------

insert into "Groceries" (grocery_name, category_id, keeping_time, minimal_amount, price)
select grocery_name,
    category_id,
    random() * (999) + 1 as keeping_time, -- <1000 days (2,7 years), 1 day>
    (random() * 200 + 1) as minimal_amount,
    floor(random() * 200 + 1) as price
from grocery_names as gn;

--------------

insert into "Inventory" (grocery_id, production_date, added, removed, amount)
select  groceries.id as grocery_id,
		null,
		null,
		null,
       	(random() * 20 + 1) as amount
from random_sub_groceries(0.75) as groceries;

insert into "Menu" (dish_name, price, recipe_content, cooking_time)
select  dish_name,
       	floor(random() * 20 + 5) as price, -- <5,25>
		random_recipe_content(),
		random() * (interval '72 hours' - interval '1 minute') as cooking_time -- <1 minute, 72 hours>
from dish_names;

insert into "Usage types" (type_name)
values ('insertion'), ('ejection');

insert into "Orders" (order_date, sum, shopping_list_id)
select now() - (random() * (now()+'3 months' - now())) as added, -- <3 months ago, now>
       floor(random() * 500 + 50) as sum,
	   null
from generate_series(1, 100) as seq(i);

insert into "Recipe groceries" (grocery_id, dish_id, amount)
select  random_grocery_id(),
       	m.id,
		floor(random() * 20) as amount
from "Menu" as m
union all
select	random_grocery_id(),
		random_dish_id(),
		floor(random() * 20) as amount
from generate_series(1,200) as seq(i);

----------------

insert into "Shopping lists" (created, insertion_id)
values (now() - (random() * (now()+'1 month' - now())), -- <1 month ago, now>
		null),
		(now() - (random() * (now()+'1 month' - now())), -- <1 month ago, now>
		null);

----------------------

insert into "Registered items" (grocery_id, inventory_id, usage_id, amount, type_id) -- insertion registration of some inventory items
select	sub_i.grocery_id,
		sub_i.id as inventory_id,
		random() * (cast((select count(*) from "Inventory")*0.6 as int) + 1) as usage_id,
		(random() * 20 + 1) as amount,
		(1) as type_id
from random_sub_inventory(0.9) as sub_i;

insert into "Registered items" (grocery_id, inventory_id, usage_id, amount, type_id) -- ejection registration of some inventory items
select	sub_i.grocery_id,
		sub_i.id as inventory_id,
		random() * (cast((select count(*) from "Inventory")*0.6 as int)) as usage_id,
		(random() * 20 + 1) as amount,
		(2) as type_id
from random_sub_inventory(0.3) as sub_i;

insert into "Registered items" (grocery_id, inventory_id, usage_id, amount, type_id) -- irregular insertions of some regular inventory items
select 	sub_g.id,
		null,
		random() * (cast((select count(*) from "Inventory")*0.6 as int)) + 1 as usage_id,
		(random() * 20 + 1) as amount,
		random() + 1 as type_id
from random_sub_groceries(0.3) as sub_g;

-----------------

insert into "Usage registration" (usage_timestamp, user_name)
select	null,
		random_user()
from
(select distinct(usage_id) from "Registered items") as usageids;

update "Usage registration" as ur
set usage_timestamp = (now() - (random() * interval '1 years' + '2 days')); -- <1 years ago, 2 days ago>

update "Inventory" as i
set added =
(select usage_timestamp
from "Usage registration" as ur
join "Registered items" as ri on ur.id=ri.usage_id
where i.id=ri.inventory_id and ri.type_id=1);

update "Inventory" as i
set removed =
(select usage_timestamp
from "Usage registration" as ur
join "Registered items" as ri on ur.id=ri.usage_id
where i.id=ri.inventory_id and ri.type_id=2);

update "Inventory"
set production_date = "Inventory".added - (random() * interval '5 years');

-- added\removed info for excessing items
update "Inventory" as inv
set added = now() - (random() * interval '20 years' + '10 years')
where inv.id in (
SELECT i.id
from "Registered items" as ri
full join "Inventory" as i on ri.inventory_id=i.id
where usage_id is null);

-- removed but not added while registered - fix
update "Inventory" as inv
set added = inv.removed + (random() * interval '3 days')
where inv.id in (
select i.id
from "Inventory" as i
join "Registered items" as ri on ri.inventory_id=i.id
where i.added is null);

----------------------

insert into "Shopping list items" (shopping_list_id, grocery_id, amount)
select	1 as shopping_list_id,
		gi.grocery_id,
		gi.amount
from get_spoiled_items() as gi
union all
select	2 as shopping_list_id,
		grocery_id,
		amount
from sample_order_items;



