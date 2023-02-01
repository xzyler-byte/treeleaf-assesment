DROP PROCEDURE IF EXISTS get_office_role_index();
DELIMITER //
CREATE PROCEDURE get_office_role_index()
BEGIN
  SELECT distinct b.* from (SELECT u.id,
                            DATE_FORMAT(u.created_date, '%Y-%m-%d') as entrydate,
                            DATE_FORMAT(u.effect_date, '%Y-%m-%d') as effectdate,
                            DATE_FORMAT(u.effect_till_date, '%Y-%m-%d') as effecttilldate,
                            u.status,
                            u.user_id as userId,
                            u.created_by as entryUserId
                     FROM user_role_assignment_master u
                     INNER JOIN user_role_assignment_detail ud ON ud.user_role_assignment_master_id = u.id
                     ORDER BY u.last_modified_date DESC) b;
END
DELIMITER //
DROP PROCEDURE IF EXISTS has_super_role(uId integer);
DELIMITER //
CREATE PROCEDURE has_super_role(uId integer)
BEGIN
            select * from get_user_roles(uId) where role_id = 1
END
DELIMITER //
DROP PROCEDURE if exists get_user_roles(uId integer);
DELIMITER //

CREATE PROCEDURE get_user_roles(uId integer)
BEGIN
    select uorm.user_id, urad.role_id
        from (
                 select uram.id, uram.user_id
                 from user_role_assignment_master uram
                 where uram.user_id = uId
                   and date(uram.effect_date) <= current_date
                   and (uram.effect_till_date is null or date(uram.effect_till_date) >= current_date)
                   and uram.status = 1
                 order by uram.last_modified_date desc
                 limit 1
             ) as uorm
                 inner join user_role_assignment_detail urad on uorm.id = urad.user_role_assignment_master_id;
END

DELIMITER //