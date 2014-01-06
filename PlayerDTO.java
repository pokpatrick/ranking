package com.f4.owl.ranking;

/**
 * @author ppk
 * @since 22/06/11 18:37
 */
public class PlayerDTO
{
    public PlayerDTO()
    {
    }

    public Long getPlayerId()
    {
        return _playerId;
    }

    public Integer getPlayerScore()
    {
        return _playerScore;
    }

    public int getPosition()
    {
        return _position;
    }

    public void setPosition( final int position )
    {
        _position = position;
    }

    public int getRank()
    {
        return _rank;
    }

    public void setRank( final int rank )
    {
        _rank = rank;
    }

    public void setPlayer( final Player player )
    {
        _playerId = player.getPlayerId();
        _playerScore = player.getPlayerScore();
    }

    private Long _playerId;
    private Integer _playerScore;
    private int _position;
    private int _rank;
}
