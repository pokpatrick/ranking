package com.f4.owl.ranking;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class RankingOpti
        extends RankingAbstract
{
    //constructor
    public RankingOpti()
    {
        _playerArrayList = new ArrayList<Player>();
		_playerhashMap = new HashMap<Long, PlayerDTO>();
    }

    //updates - inserts
    @Override
    public void updateRankingReplace( @NotNull final Long playerId, @NotNull final Integer playerScore )
    {
        _readWriteLock.writeLock().lock();
final Player player = new Player( playerId, playerScore );
int i;
final Comparator<Player> comp = getComparator();
        try
        {
		PlayerDTO playerDTO = _playerHashMap.get(playerId);
		if(playerDTO != null)
		{
			i = playerDTO.getPosition;
			_playerArrayList.remove(i);
			if(comp.compare(player, playerDTO;getplayerScore) > 0 )
	{
while( ( i > 0) &&
                    ( comp.compare( player, _playerArray.get( i ) ) > 0 ) )
            {
		i--;
}
}
		}
		else
		{
			i = 0;
			while( ( i < _playerArray.size() ) &&
                    ( comp.compare( player, _playerArray.get( i ) ) > 0 ) )
            {
                i++;
            }

		}

		 while( ( i < _playerArray.size() ) &&
                    ( comp.compare( player, _playerArray.get( i ) ) > 0 ) )
            {
                i++;
            }
            _playerArray.add( i, player );
		_playerHashMap.put(playerId, new PlayerDTO(playerId, playerScore, i, null);
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
        
        try
        {

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
PlayerDTO playerDTO = _playerHashMap.get(playerId);
        try
        {
if(player != null)
			{
				_playerArrayList.remove(playerDTO.getPosition);
				_playerHashMap.remove(playerId);
	deleteData( playerId );

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
	PlayerDTO playerDTO = _playerHashMap.get(playerId);
        try
        {
			if(player != null)
			{
				return(playerDTO.getPosition + 1);
			}
else
			{
				retuen(null);
			}

        }
        finally
        {
            _readWriteLock.readLock().unlock();
        }
    }

    @Nullable
    @Override
    public Player getPlayerAtPosition( final int position )
    {
        _readWriteLock.readLock().lock();
        try
        {
            if( ( position < 1 ) || ( position > _playerArray.size() ) )
            {
                return ( null );
            }
            else
            {
                return ( _playerArray.get( position - 1 ) );
            }
        }
        finally
        {
            _readWriteLock.readLock().unlock();
        }
    }

  
    @Nullable
    @Override
    public Integer getPlayerRank( @NotNull final Long playerId )
    {
        int i;
        int j;
        final Integer actualScore;

        _readWriteLock.readLock().lock();
        try
        {
            for( i = 0; i < _playerArray.size(); i++ )
            {
                if( _playerArray.get( i ).getPlayerId().equals( playerId ) )
                {
                    if( i == 0 )
                    {
                        return ( 1 );
                    }
                    actualScore = _playerArray.get( i ).getPlayerScore();
                    j = i;
                    while( ( j > 0 ) && _playerArray.get( j - 1 ).getPlayerScore().equals( actualScore ) )
                    {
                        j--;
                    }
                    return ( j + 1 );
                }
            }
            return ( null );
        }
        finally
        {
            _readWriteLock.readLock().unlock();
        }
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
            final Player player = getPlayerAtPosition( 1 );
            if( null != player )
            {
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
        int i = 0;
        _readWriteLock.readLock().lock();
        try
        {
            final ArrayList<PlayerDTO> result = new ArrayList<PlayerDTO>();

            for( final Player player : _playerArray )
            {
                if( ( player != null ) && ( i < 5 ) )
                {
                    final PlayerDTO playerDTO = new PlayerDTO();
                    playerDTO.setPlayer( player );
                    playerDTO.setPosition( getPlayerPosition( player.getPlayerId() ) );
                    playerDTO.setRank( getPlayerRank( player.getPlayerId() ) );
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
        if( n < 1 )
        {
            n = 1;
        }
        if( m > _playerArray.size() )
        {
            m = _playerArray.size();
        }
        int i;
        _readWriteLock.readLock().lock();
        try
        {
            final ArrayList<PlayerDTO> result = new ArrayList<PlayerDTO>();
            for( i = n - 1; i < m; i++ )
            {
                final PlayerDTO playerDTO = new PlayerDTO();
                playerDTO.setPlayer( _playerArray.get( i ) );
                playerDTO.setPosition( getPlayerPosition( _playerArray.get( i ).getPlayerId() ) );
                playerDTO.setRank( getPlayerRank( _playerArray.get( i ).getPlayerId() ) );
                result.add( playerDTO );
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
        return _playerArrayList.size();
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


    //attributs - constants
    private ArrayList<Player> _playerArrayList;
private HashMap<Long, PlayerDTO> _playerHashMap;

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
