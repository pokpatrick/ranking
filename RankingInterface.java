package com.f4.owl.ranking;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author ppk
 * @since 27/05/11 16:11
 */
public interface RankingInterface
{
    void updateRankingReplace( @NotNull Long playerId, @NotNull Integer playerScore );

    void updateRankingSum( @NotNull Long playerId, @NotNull Integer playerScore );

    void updateRankingBetter( @NotNull Long playerId, @NotNull Integer playerScore );

    void deletePlayer( @NotNull Long playerId );

    @Nullable
    Integer getPlayerPosition( @NotNull Long playerId );

    @Nullable
    Player getPlayerAtPosition( int position );

    @Nullable
    Integer getPlayerRank( @NotNull Long playerId );

    @Nullable
    Integer getPlayerPage( @NotNull Long playerId, int avatarPerPage );

    @Nullable
    PlayerDTO getFirstPlayer();

    @Nullable
    List<PlayerDTO> getTopFivePlayers();

    List<PlayerDTO> getPlayersBetween( @NotNull Integer n, @NotNull Integer m );

    List<PlayerDTO> getRankingPages( @NotNull Integer page, @NotNull Integer numberPerPage );

    int getNumberOfPlayers();

    public String getPartitionArg( @NotNull Long playerId );

    void setOrder( @NotNull Order order );
}
