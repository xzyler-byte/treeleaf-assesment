DROP PROCEDURE IF EXISTS get_menus_by_user(uId integer);
DELIMITER //

CREATE PROCEDURE get_menus_by_user(uId integer)
BEGIN
	CALL get_user_roles(@userRoles);
   select distinct
            menus.roleids,
            menus.menu_id,
            menus.menu_name,
            menus.menu_code,
            menus.icon_class,
            menus.master_menu_id,
            menus.order_id,
            menus.url,
            menus.is_hidden,
            menus.controller_name,
            case when iscreatetruecount > 0 then true else false end as iscreate,
            case when isupdatetruecount > 0 then true else false end as isupdate,
            case when isdeletetruecount > 0 then true else false end as isdelete,
            case when isquerytruecount > 0 then true else false end as isview,
            case when iseditapprovedtruecount > 0 then true else false end as isapprove,
            case when isreporttruecount > 0 then true else false end as isreport
        from (
                 select distinct GROUP_CONCAT(usmam.role_id)  as roleids,
                                 usmad.menu_id,
                                 am.menu_name,
                                 am.menu_code,
                                 am.icon_class,
                                 am.master_menu_id,
                                 am.order_id,
                                 am.url,
                                 am.is_hidden,
                                 sum(case when usmad.is_create = true then 1 else 0 end) as iscreatetruecount,
                                 sum(case when usmad.is_update = true then 1 else 0 end) as isupdatetruecount,
                                 sum(case when usmad.is_delete = true then 1 else 0 end) as isdeletetruecount,
                                 sum(case when usmad.is_query = true then 1 else 0 end) as isquerytruecount,
                                 sum(case when usmad.is_approve = true then 1 else 0 end) as iseditapprovedtruecount,
                                 sum(case when usmad.is_report = true then 1 else 0 end) as isreporttruecount
                 from (
                          select um.id,
                                 um.role_id,
                                 row_number()
                                 over (partition by um.role_id order by um.effect_date desc, um.last_modified_date desc) as rownum
                          from role_menu_right_assignment_master um
                          where um.role_id in (SELECT role_id FROM userRoles)
                            and date(um.effect_date) <= current_date
                            and (um.effect_till_date is null or date(um.effect_till_date) >= current_date)
                            and um.status = 1
                          order by um.last_modified_date desc
                      ) as usmam
                          inner join role_menu_right_assignment_detail usmad
                                     on usmam.id = usmad.role_menu_right_assignment_master_id and usmad.is_query = true
                          inner join menu am on usmad.menu_id = am.id and am.is_hidden = false
                 where usmam.rownum = 1
                 group by usmad.menu_id, am.menu_name, am.menu_code,
                          am.icon_class, am.master_menu_id,
                          am.order_id, am.url, am.is_hidden order by am.order_id asc
             ) as menus order by menus.order_id asc;
END
DELIMITER //
DROP PROCEDURE IF EXISTS get_all_menus();

DELIMITER //

CREATE PROCEDURE get_all_menus()
    RETURN
        select
            '',
            m.id,
            m.menu_name,
            m.menu_code,
            m.icon_class,
            m.master_menu_id,
            m.order_id,
            m.url,
            m.is_Hidden,
            true as iscreate,
            true as isupdate,
            true as isdelete,
            true as isview,
            true as isapprove,
            true as isreport
        from menu m where m.status = 1 and m.is_hidden = false
        order by m.master_menu_id, m.order_id;
END
DELIMITER //