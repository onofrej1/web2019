<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Auth;

class UserController extends ResourceController {

    public $model = 'App\User';

    function login(Request $request) {
        if (Auth::attempt(['email' => $request->email, 'password' => $request->password])) {
            return response('Successfully logged in.', 201);
        } else {
            return response([
                'message' => ['These credentials do not match our records.']
            ], 404);
        }
    }

    function logout() {
        Auth::logout();

        return response('logged out', 201);
    }
}
