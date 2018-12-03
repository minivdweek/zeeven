package nl.mini.zeeven.controller;

import nl.mini.zeeven.exception.ResourceNotFoundException;
import nl.mini.zeeven.model.Player;
import nl.mini.zeeven.repository.PlayerRepository;
import nl.mini.zeeven.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @GetMapping("/teams/{teamId}/players")
    public List<Player> getPlayersByTeamId(@PathVariable Long teamId) {
        return playerRepository.findByTeamId(teamId);
    }

    @PostMapping("/teams/{teamId}/players")
    public Player addPlayer(@PathVariable Long teamId, @Valid @RequestBody Player player) {
        return teamRepository.findById(teamId)
                .map(team -> {
                    player.setTeam(team);
                    return playerRepository.save(player);
                }).orElseThrow(() -> new ResourceNotFoundException(String.format("Team with id '%d' not found.", teamId)));
    }

    @PutMapping("/teams/{teamId}/players/{playerId}")
    public Player updatePlayer(@PathVariable Long teamId, @PathVariable Long playerId, @Valid @RequestBody Player player) {
        ensureTeamExists(teamId);

        return playerRepository.findById(playerId)
                .map(p -> {
                    p.setName(player.getName());
                    p.setInfo(player.getInfo());
                    return playerRepository.save(p);
                }).orElseThrow(() -> new ResourceNotFoundException(String.format("Player with id '%d' not found.", playerId)));
    }

    @DeleteMapping("/teams/{teamId}/players/{playerId}")
    public ResponseEntity<?> deletePlayer(@PathVariable Long teamId, @PathVariable Long playerId) {
        ensureTeamExists(teamId);

        return playerRepository.findById(playerId)
                .map(p -> {
                    playerRepository.delete(p);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException(String.format("Player with id '%d' not found.", playerId)));

    }

    private void ensureTeamExists(final Long teamId) {
        if (!teamRepository.existsById(teamId)) {
            throw new ResourceNotFoundException(String.format("Team with id '%d' not found.", teamId));
        }
    }
}
