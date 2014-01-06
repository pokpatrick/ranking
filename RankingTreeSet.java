package com.f4.owl.ranking;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public class RankingTreeSet
        extends RankingAbstract
{
    //constructor
    public RankingTreeSet()
    {
        _playerTreeSet = null;
    }

    //updates - inserts
    @Override
    public void updateRankingReplace( @NotNull final Long playerId, @NotNull final Integer playerScore )
    {
        _readWriteLock.writeLock().lock();
        try
        {
            final Iterator<Player> iterator = _playerTreeSet.iterator();
            while( iterator.hasNext() )
            {
                if( iterator.next().getPlayerId().equals( playerId ) )
                {
                    iterator.remove();
                    break;
                }
            }
            _playerTreeSet.add( new Player( playerId, playerScore ) );
        }
        finally
        {
            _readWriteLock.writeLock().unlock();
            writeData( playerId, playerScore );
        }
    }

    @Override
    public void updateRankingSum( @NotNull final Long playerId, @NotNull final Integer playerScore )
    {
        _readWriteLock.writeLock().lock();
        int oldScore = 0;
        try
        {
            final Iterator<Player> iterator = _playerTreeSet.iterator();
            while( iterator.hasNext() )
            {
                final Player iteratorPlayer = iterator.next();
                if( iteratorPlayer.getPlayerId().equals( playerId ) )
                {
                    oldScore = iteratorPlayer.getPlayerScore();
                    iterator.remove();
                    break;
                }
            }
            _playerTreeSet.add( new Player( playerId, playerScore + oldScore ) );
        }
        finally
        {
            _readWriteLock.writeLock().unlock();
            writeData( playerId, playerScore + oldScore );
        }
    }

    @Override
    public void updateRankingBetter( @NotNull final Long playerId, @NotNull final Integer playerScore )
    {
        _readWriteLock.writeLock().lock();
        try
        {
            final Player player = new Player( playerId, playerScore );
            final Iterator<Player> iterator = _playerTreeSet.iterator();
            while( iterator.hasNext() )
            {
                final Player iteratorPlayer = iterator.next();
                if( iteratorPlayer.getPlayerId().equals( playerId ) )
                {
                    if( _playerTreeSet.comparator().compare( player, iteratorPlayer ) < 0 )
                    {
                        iterator.remove();
                        break;
                    }
                    else
                    {
                        return;
                    }
                }
            }
            _playerTreeSet.add( player );
        }
        finally
        {
            _readWriteLock.writeLock().unlock();
            writeData( playerId, playerScore );
        }
    }

    //delete
    @Override
    public void deletePlayer( @NotNull final Long playerId )
    {
        _readWriteLock.writeLock().lock();
        try
        {
            final Iterator<Player> iterator = _playerTreeSet.iterator();
            while( iterator.hasNext() )
            {
                if( iterator.next().getPlayerId().equals( playerId ) )
                {
                    iterator.remove();
                    deleteData( playerId );
                }
            }
        }
        finally
        {
            _readWriteLock.writeLock().unlock();
        }
    }

    //getters
    @Nullable
    @Override
    public Integer getPlayerPosition( @NotNull final Long playerId )
    {
        _readWriteLock.readLock().lock();
        Integer position = 0;
        try
        {
            for( final Player player : _playerTreeSet )
            {
                position++;
                if( player.getPlayerId().equals( playerId ) )
                {
                    return ( position );
                }
            }
        }
        finally
        {
            _readWriteLock.readLock().unlock();
        }
        return ( null );
    }

    @Nullable
    @Override
    public Player getPlayerAtPosition( final int position )
    {
        _readWriteLock.readLock().lock();
        try
        {
            final Iterator<Player> iterator = _playerTreeSet.iterator();
            Player iteratorPlayer;
            int i = position;

            if( position < 1 )
            {
                return ( null );
            }

            while( iterator.hasNext() )
            {
                iteratorPlayer = iterator.next();
                if( i <= 1 )
                {
                    return ( iteratorPlayer );
                }
                else
                {
                    i--;
                }
            }
        }
        finally
        {
            _readWriteLock.readLock().unlock();
        }
        return ( null );
    }

    @Nullable
    @Override
    public Integer getPlayerRank( @NotNull final Long playerId )
    {
        _readWriteLock.readLock().lock();
        int i = 0;
        try
        {
            final Integer score = getPlayerScore( playerId );
            for( final Player iteratorPlayer : _playerTreeSet )
            {
                i++;
                if( iteratorPlayer.getPlayerScore().equals( score ) )
                {
                    return ( i );
                }
            }
        }
        finally
        {
            _readWriteLock.readLock().unlock();
        }
        return ( null );
    }

    @Nullable
    @Override
    public Integer getPlayerPage( @NotNull final Long playerId, final int avatarPerPage )
    {
        final Integer position = getPlayerPosition( playerId );

        if( position == null )
        {
            return ( null );
        }
        else
        {
            return ( (int) ( Math.ceil( (double) position / (double) avatarPerPage ) ) );
        }
    }

    @Nullable
    @Override
    public PlayerDTO getFirstPlayer()
    {
        _readWriteLock.readLock().lock();
        try
        {
            if( !_playerTreeSet.isEmpty() )
            {
                final Player player = _playerTreeSet.first();
                final PlayerDTO playerDTO = new PlayerDTO();
                playerDTO.setPlayer( player );
                playerDTO.setPosition( getPlayerPosition( player.getPlayerId() ) );
                playerDTO.setRank( getPlayerRank( player.getPlayerId() ) );
                return ( playerDTO );
            }
            else
            {
                return ( null );
            }
        }
        finally
        {
            _readWriteLock.readLock().unlock();
        }
    }

    @Nullable
    @Override
    public List<PlayerDTO> getTopFivePlayers()
    {
        _readWriteLock.readLock().lock();
        int i = 0;
        try
        {
            final ArrayList<PlayerDTO> result = new ArrayList<PlayerDTO>();
            for( final Player iteratorPlayer : _playerTreeSet )
            {
                if( ( iteratorPlayer != null ) && ( i < 5 ) )
                {
                    final PlayerDTO playerDTO = new PlayerDTO();
                    playerDTO.setPlayer( iteratorPlayer );
                    playerDTO.setPosition( getPlayerPosition( iteratorPlayer.getPlayerId() ) );
                    playerDTO.setRank( getPlayerRank( iteratorPlayer.getPlayerId() ) );
                    result.add( playerDTO );
                }
                else
                {
                    break;
                }
                i++;
            }
            return ( result );
        }
        finally
        {
            _readWriteLock.readLock().unlock();
        }
    }

    @Override
    public List<PlayerDTO> getPlayersBetween( @NotNull Integer n, @NotNull Integer m )
    {
        if( n <= 1 )
        {
            n = 1;
        }
        if( m >= _playerTreeSet.size() )
        {
            m = _playerTreeSet.size();
        }

        _readWriteLock.readLock().lock();
        try
        {
            final List<PlayerDTO> result = new ArrayList<PlayerDTO>();
            int i = 0;
            for( final Player player : _playerTreeSet )
            {
                i++;
                if( i >= n )
                {
                    final PlayerDTO playerDTO = new PlayerDTO();
                    playerDTO.setPlayer( player );
                    playerDTO.setPosition( getPlayerPosition( player.getPlayerId() ) );
                    playerDTO.setRank( getPlayerRank( player.getPlayerId() ) );
                    result.add( playerDTO );
                }
                if( i >= m )
                {
                    break;
                }
            }
            return ( result );
        }
        finally
        {
            _readWriteLock.readLock().unlock();
        }
    }

    @Override
    public List<PlayerDTO> getRankingPages( @NotNull final Integer page, @NotNull final Integer numberPerPage )
    {
        return ( getPlayersBetween( ( ( page - 1 ) * numberPerPage ) + 1, ( ( page - 1 ) * numberPerPage ) + numberPerPage ) );
    }

    public int getNumberOfPlayers()
    {
        return _playerTreeSet.size();
    }

    //setters
    @Override
    public void setOrder( @NotNull final Order order )
    {
        super.setOrder( order );
        _playerTreeSet = new TreeSet<Player>( getComparator() );
    }

    @Override
    protected void clearDataInMemory()
    {
        _playerTreeSet.clear();
    }

    @Override
    protected void updateDataInMemory( @NotNull final List<Player> list )
    {
        for( final Player player : list )
        {
            updateRankingReplace( player.getPlayerId(), player.getPlayerScore() );
        }
    }

    @Nullable
    private Integer getPlayerScore( @NotNull final Long playerId )
    {
        _readWriteLock.readLock().lock();
        try
        {
            for( final Player iteratorPlayer : _playerTreeSet )
            {
                if( iteratorPlayer.getPlayerId().equals( playerId ) )
                {
                    return ( iteratorPlayer.getPlayerScore() );
                }
            }
        }
        finally
        {
            _readWriteLock.readLock().unlock();
        }
        return ( null );
    }

    //attributs - constants
    private TreeSet<Player> _playerTreeSet;

/*
    @Override
    public String toString()
    {
        String ret = "";

        _readWriteLock.readLock().lock();
        try
        {
            for( final Player a_playerTreeSet : _playerTreeSet )
            {
                ret = ret + a_playerTreeSet.toString() + '\n';
            }
        }
        finally
        {
            _readWriteLock.readLock().unlock();
        }
        return ( "name" + '\n' + ret );
    }*/
}
