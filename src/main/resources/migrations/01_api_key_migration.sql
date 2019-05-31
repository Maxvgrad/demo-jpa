-- Remove duplication teams

--v1
select t.id, t2.id
FROM team t
       LEFT OUTER JOIN (
    SELECT max(tt.id) as id, tt.api_football_com_team_id
    FROM team tt
    where tt.api_football_com_team_id is not null
    GROUP BY api_football_com_team_id
    having count(1) > 1
  ) as t2 ON t.id = t2.id
WHERE 1 = 1
  and t2.id IS not NULL;

--v2
delete
from team
where id in (
  select t.id
  from team t,
       team e
  where 1 = 1
    and t.checked
    and e.checked
    and t.api_football_com_team_id is not null
    and e.api_football_com_team_id is not null
    and t.api_football_com_team_id = e.api_football_com_team_id
    and t.id > e.id
);

--v3
with doubles as (select t.*
                 from team t
                        join team t2 on t.id = t2.id and
                                        t.api_football_com_team_id in (
                                          select t.api_football_com_team_id
                                          from team t
                                          where t.api_football_com_team_id is not null
                                          group by t.api_football_com_team_id
                                          having count(1) > 1)
                 where 1 = 1
                   and t.founded = t2.founded
                   and t.logo = t2.logo
                   and t.name = t2.name
                   and t.api_football_com_team_id = t2.api_football_com_team_id
)
select t.*
from team t
       join doubles d on t.id = d.id
where 1 = 1
  and exists(
    select e.id
    from team e
    where 1 = 1
      and e.api_football_com_team_id = t.api_football_com_team_id
      and e.id < t.id
  );

-- Migration date into api tables

insert into api_team (api_id, team_id)
    (select t.api_football_com_team_id, t.id from team t);

insert into api_player (api_id, player_id)
    (select t.api_football_com_player_id, t.id from player t);

insert into season (id, is_current, season, season_end, season_start, standings, league_id)
  (select l.id,
          l.is_current,
          l.season,
          l.season_end,
          l.season_start,
          l.standings,
          (select min(l2.id)
           from league l2
           where l2.name = l.name
           group by l2.name)
   from League l);

select count(1)
from season s
       join league l on s.id = l.id
       join league l2 on l2.id = s.id
where l.name != l2.name;

insert into api_league (api_id, season_id)
    (select l.api_football_com_league_id, l.id from league l);

-- delete league duplications

delete from league l where l.id not in (
  select min(l2.id) from league l2
  group by l2.name);