package nl.mini.zeeven.controller;

import nl.mini.zeeven.exception.ResourceNotFoundException;
import nl.mini.zeeven.model.Team;
import nl.mini.zeeven.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class TeamController {

    @Autowired
    private TeamRepository teamRepository;

    @GetMapping("/teams")
    public Page<Team> getTeamss(Pageable pageable) {
        return teamRepository.findAll(pageable);
    }

    @PostMapping("/teams")
    public Team createTeam(@Valid @RequestBody Team team) {
        return teamRepository.save(team);
    }

    @PutMapping("/teams/{teamId}")
    public Team updateTeam(@PathVariable Long teamId, @Valid @RequestBody Team team) {
        return teamRepository.findById(teamId)
                .map(t -> {
                    t.setTitle(team.getTitle());
                    t.setDescription(team.getDescription());
                    return teamRepository.save(t);
                }).orElseThrow(() -> new ResourceNotFoundException(String.format("Team with id '%d' not found.", teamId)));
    }

    @DeleteMapping("/teams/{teamId}")
    public ResponseEntity<?> deleteTeam(@PathVariable Long teamId) {
        return teamRepository.findById(teamId)
                .map(team -> {
                    teamRepository.delete(team);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException(String.format("Team with id '%d' not found.", teamId)));
    }
}
