package com.f4.owl.ranking;

import com.f4.owl.parting.Countries;
import com.f4.owl.persistence.RankingDAO;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.scheduling.support.CronTrigger;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author ppk
 * @since 17/06/11 10:44
 */
public abstract class RankingAbstract
        implements RankingInterface
{
    public void setOrder( @NotNull final Order order )
    {
        _order = order;
    }

    protected abstract void clearDataInMemory();

    protected abstract void updateDataInMemory( @NotNull final List<Player> list );

    //start
    public void start()
    {
        restoreData();
        if( null != _cronExpression && !_cronExpression.isEmpty() )
        {
            final ClearDataDate lastClearDate = _rankingDAO.getLastClearDateFromDataBase();
            final Date currentStart = new Date();
            final CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator( _cronExpression, TimeZone.getDefault() );

            if(lastClearDate != null)
            {
                final Date nextCronFire = cronSequenceGenerator.next( lastClearDate.getDate() );

                if( currentStart.after( nextCronFire ) )
                {
                    clearData();
                }
            }
            else
            {
                _rankingDAO.writeLastClearDateInDataBase( currentStart );
            }

            _scheduler.schedule( new InnerRunnable(), new CronTrigger( _cronExpression ) );
        }
    }

    //database
    protected void writeData( @NotNull final Long playerId, @NotNull final Integer playerScore )
    {
        _rankingDAO.writeDataInDataBase( playerId, playerScore );
    }

    protected void deleteData( @NotNull final Long playerId )
    {
        _rankingDAO.deleteDataInDataBase( playerId );
    }

    protected void clearData()
    {
        final Date currentClearDate = new Date();
        _rankingDAO.clearDataInDataBase();
        clearDataInMemory();
        _rankingDAO.writeLastClearDateInDataBase( currentClearDate );
    }

    protected void restoreData()
    {
        clearDataInMemory();
        updateDataInMemory( _rankingDAO.restoreDataFromDataBase() );
    }

    protected Comparator<Player> getComparator()
    {
        if( null != _order )
        {
            switch( _order )
            {
                case DECREASING:
                    return new FirstHaveMostPointsComparator();
                case INCREASING:
                    return new FirstHaveLessPointsComparator();
            }
        }
        throw new RuntimeException( "order unknown" );
    }

    //setters Spring
    public void setRankingDAO( final RankingDAO rankingDAO )
    {
        _rankingDAO = rankingDAO;
    }

    public void setScheduler( final TaskScheduler scheduler )
    {
        _scheduler = scheduler;
    }

    public void setCronExpression( final String cronExpression )
    {
        _cronExpression = cronExpression;
    }

    private class InnerRunnable
            implements Runnable
    {
        @Override
        public void run()
        {
            clearData();
        }
    }

    public String getPartitionArg( @NotNull Long playerId )
    {
        switch((int) (Math.random() * 3) + 1)
        {
            case 1 : 
                return(String.valueOf( Countries.SPAIN ));
            case 2 :
                return(String.valueOf( Countries.ITALY ));
            case 3 :
                return(String.valueOf( Countries.FRANCE ));
            default :
                return(String.valueOf( Countries.GERMANY ));
        }
    }

    protected ReadWriteLock _readWriteLock = new ReentrantReadWriteLock();
    private RankingDAO _rankingDAO;
    private TaskScheduler _scheduler;
    private String _cronExpression;
    private Order _order;
}
