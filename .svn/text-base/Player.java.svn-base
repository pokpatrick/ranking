package com.f4.owl.ranking;

import org.jetbrains.annotations.NotNull;

public class Player
{
    public Player()
    {
    }

    public Player( @NotNull final Long playerId, @NotNull final Integer score )
    {
        _playerId = playerId;
        _playerScore = score;
    }

    @Override
    public String toString()
    {
        return ( "Player : " + _playerId + "; Score : " + _playerScore );
    }

    @Override
    public boolean equals( final Object o )
    {
        if( this == o ) return true;
        if( !( o instanceof Player ) ) return false;

        final Player player = (Player) o;

        if( !_playerId.equals( player._playerId ) ) return false;
        if( !_playerScore.equals( player._playerScore ) ) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = _playerId.hashCode();
        result = 31 * result + _playerScore.hashCode();
        return result;
    }

    public Long getPlayerId()
    {
        return _playerId;
    }

    public void setPlayerId( final Long playerId )
    {
        _playerId = playerId;
    }

    public Integer getPlayerScore()
    {
        return _playerScore;
    }

    public void setPlayerScore( final Integer playerScore )
    {
        _playerScore = playerScore;
    }

    private Long _playerId;
    private Integer _playerScore;
}
