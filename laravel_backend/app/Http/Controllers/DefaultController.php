<?php
namespace App\Http\Controllers;

use App\Models\Event;
use App\Models\Run;
use App\Models\Runner;
use App\Models\Result;


class DefaultController extends Controller
{

    /**
    * Show the profile for the given user.
    *
    */
    public function test()
    {
        $events = Event::all();
        // dump($events->first());

        return $events;
        // return view('user.profile', ['user' => User::findOrFail($id)]);
    }
}
