<?php

namespace App\Http\Controllers;

use App\User;
use Illuminate\Auth\Events\Registered;
use Illuminate\Http\Request;
use Auth;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Validator;

class UserController extends ResourceController {

    public $model = 'App\User';

    protected function guard()
    {
        return Auth::guard();
    }

    function login(Request $request) {
        if (Auth::attempt(['email' => $request->email, 'password' => $request->password])) {
            return response('Successfully logged in.', 201);
        } else {
            return response([
                'message' => ['These credentials do not match our records.']
            ], 404);
        }
    }

    protected function validator(array $data)
    {
        return Validator::make($data, [
            'name' => ['required', 'string', 'max:255'],
            'email' => ['required', 'string', 'email', 'max:255', 'unique:users'],
            'password' => ['required', 'string', 'min:8', 'confirmed'],
        ]);
    }

    protected function createUser(array $data)
    {
        return User::create([
            'name' => $data['name'],
            'email' => $data['email'],
            'password' => Hash::make($data['password']),
        ]);
    }

    public function register(Request $request)
    {
        $this->validator($request->all())->validate();
        event(new Registered($user = $this->createUser($request->all())));

        $this->guard()->login($user);

        return response('Successfully registered.', 201);
    }

    function logout() {
        Auth::logout();

        return response('logged out', 201);
    }
}
