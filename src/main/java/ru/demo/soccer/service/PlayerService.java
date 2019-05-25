package ru.demo.soccer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ru.demo.soccer.dto.PlayerFilter;
import ru.demo.soccer.entities.Player;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class PlayerService {

    private final EntityManager entityManager;

    /**
     * Search for player
     *
     * @param filter - player filter
     */
    public List<Player> search(PlayerFilter filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Player> cq = cb.createQuery(Player.class);
        Root<Player> player = cq.from(Player.class);
        Predicate criteria = cb.conjunction();

        if (filter.getInjured() != null) {
            criteria = cb.equal(player.get("injured"), filter.getInjured());
        }
        if (filter.getAge() != null) {
            criteria = cb.and(criteria, cb.equal(player.get("age"), filter.getAge()));
        }
        if (StringUtils.isNotBlank(filter.getCaptain())) {
            criteria = cb.and(criteria, cb.equal(player.get("captain"), filter.getCaptain()));
        }
        if (StringUtils.isNotBlank(filter.getPlayerName())) {
            criteria = cb.and(criteria, cb.equal(player.get("player_name"), filter.getPlayerName()));
        }
        if (StringUtils.isNotBlank(filter.getPosition())) {
            criteria = cb.and(criteria, cb.equal(player.get("position"), filter.getPosition()));
        }
        if (StringUtils.isNotBlank(filter.getTeamName())) {
            criteria = cb.and(criteria, cb.equal(player.get("team").get("name"), filter.getTeamName()));
        }
        if (filter.getNumber() != null) {
            criteria = cb.and(criteria, cb.equal(player.get("number"), filter.getNumber()));
        }
        cq.select(player).where(criteria);
        return entityManager.createQuery(cq).getResultList();
    }


}
