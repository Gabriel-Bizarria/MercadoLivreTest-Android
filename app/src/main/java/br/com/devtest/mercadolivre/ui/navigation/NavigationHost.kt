package br.com.devtest.mercadolivre.ui.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.devtest.mercadolivre.ui.screens.productdetail.ProductDetailScreen
import br.com.devtest.mercadolivre.ui.screens.search.SearchScreen
import br.com.devtest.mercadolivre.ui.viewmodels.SearchViewModel
import br.com.devtest.mercadolivre.ui.screens.searchresult.SearchResultListScreen
import br.com.devtest.mercadolivre.ui.viewmodels.ProductDetailViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val searchViewModel = koinViewModel<SearchViewModel>()

    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.SearchScreen,
        modifier = modifier
            .padding(WindowInsets.systemBars.asPaddingValues())

    ) {
        composable<NavigationRoutes.SearchScreen> {
            SearchScreen(
                viewModel = searchViewModel,
                navigateToSearchResults = {
                    navController.navigate(NavigationRoutes.SearchResultScreen)
                }
            )
        }

        composable<NavigationRoutes.SearchResultScreen> {
            SearchResultListScreen(viewModel = searchViewModel, onItemClick = { id ->
                navController.navigate(NavigationRoutes.ProductDetailScreen(id))
            })
        }

        composable<NavigationRoutes.ProductDetailScreen> { backstackEntry ->
            val viewModel = koinViewModel<ProductDetailViewModel>(viewModelStoreOwner = backstackEntry)
            ProductDetailScreen(
                viewModel = viewModel,
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

