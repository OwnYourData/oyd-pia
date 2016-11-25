package eu.ownyourdata.pia.web.rest.dto;

import eu.ownyourdata.pia.domain.RepoItemCount;

import java.util.Set;

public class PluginStatsDTO {

    private Long id;

    private Set<RepoItemCount> repoItemCounts;

    public PluginStatsDTO(Long id, Set<RepoItemCount> repoItemCounts) {
        this.id = id;
        this.repoItemCounts = repoItemCounts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<RepoItemCount> getRepoItemCounts() {
        return repoItemCounts;
    }

    public void setRepoItemCounts(Set<RepoItemCount> repoItemCounts) {
        this.repoItemCounts = repoItemCounts;
    }
}
