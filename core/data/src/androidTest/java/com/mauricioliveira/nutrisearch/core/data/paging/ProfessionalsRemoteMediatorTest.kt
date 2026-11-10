package com.mauricioliveira.nutrisearch.core.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator.MediatorResult
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mauricioliveira.nutrisearch.core.database.NutriSearchDatabase
import com.mauricioliveira.nutrisearch.core.database.entity.ProfessionalEntity
import com.mauricioliveira.nutrisearch.core.domain.model.AppError
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalPagingApi::class)
@RunWith(AndroidJUnit4::class)
class ProfessionalsRemoteMediatorTest {

    private lateinit var db: NutriSearchDatabase
    private val server = FakeServer(all = (1..10).map(::aDto))

    // The mediator only reads config.pageSize from the state, so an empty state is enough.
    private val state = PagingState<Int, ProfessionalEntity>(
        pages = emptyList(),
        anchorPosition = null,
        config = PagingConfig(pageSize = 4),
        leadingPlaceholderCount = 0,
    )

    private fun mediator() = ProfessionalsRemoteMediator(sortBy = SORT, api = server.api, database = db)

    private suspend fun storedIds() = db.searchResultDao().idsFor(SORT)

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NutriSearchDatabase::class.java,
        ).build()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun refreshThenAppend_storesPagesInServerOrder() = runTest {
        val mediator = mediator()

        val refresh = mediator.load(LoadType.REFRESH, state)

        assertTrue(refresh is MediatorResult.Success)
        assertFalse((refresh as MediatorResult.Success).endOfPaginationReached)
        assertEquals(listOf(1, 2, 3, 4), storedIds())
        assertEquals(4, db.remoteKeyDao().get(SORT)?.nextOffset)

        mediator.load(LoadType.APPEND, state)

        assertEquals("4", server.requests.last().parameters["offset"])
        assertEquals((1..8).toList(), storedIds())
    }

    @Test
    fun appendOnLastPage_reachesEndAndStopsRequesting() = runTest {
        val mediator = mediator()
        mediator.load(LoadType.REFRESH, state) // 1..4
        mediator.load(LoadType.APPEND, state)  // 5..8

        val last = mediator.load(LoadType.APPEND, state) // 9..10, only 2 items

        assertTrue((last as MediatorResult.Success).endOfPaginationReached)
        assertNull(db.remoteKeyDao().get(SORT)?.nextOffset)
        assertEquals((1..10).toList(), storedIds())

        val requestsBefore = server.requests.size
        val afterEnd = mediator.load(LoadType.APPEND, state)
        assertTrue((afterEnd as MediatorResult.Success).endOfPaginationReached)
        assertEquals(requestsBefore, server.requests.size) // no network call past the end
    }

    @Test
    fun refreshWhileOffline_returnsErrorAndKeepsCache() = runTest {
        val mediator = mediator()
        mediator.load(LoadType.REFRESH, state)
        server.offline = true

        val result = mediator.load(LoadType.REFRESH, state)

        assertTrue(result is MediatorResult.Error)
        assertEquals(AppError.NoConnection, (result as MediatorResult.Error).throwable)
        assertEquals(listOf(1, 2, 3, 4), storedIds())
    }

    private companion object {
        const val SORT = "rating"
    }
}