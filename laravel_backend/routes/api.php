<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::middleware('auth:sanctum')->get('/user', function (Request $request) {
    return $request->user();
});

/* Route::middleware('auth:api')->get('/user', function (Request $request) {
    return $request->user();
}); */



Route::get('test', 'DefaultController@test');
// Route::get('resource/:resource', 'ResourceController@index');

Route::resource('runs', 'RunController');
Route::resource('events', 'EventController');
Route::resource('articles', 'ArticleController');
Route::resource('menuItems', 'MenuItemController');
Route::resource('pages', 'PageController');
Route::resource('news', 'NewsController');
Route::resource('menus', 'MenuController');
Route::resource('runners', 'RunnerController');
Route::resource('categories', 'CategoryController');
Route::resource('tags', 'TagController');
Route::resource('results', 'ResultController');
Route::resource('users', 'UserController');
Route::resource('roles', 'RoleController');
Route::resource('permissions', 'PermissionController');

Route::post('login', 'UserController@login');
Route::get('logout', 'UserController@logout');






