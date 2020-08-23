<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class EventController extends ResourceController
{
    public $model = 'App\Models\Event';

    public function show(Request $request, $id)
    {
        $model = $this->model::with('run')->find($id);

        return response()->json($model);
    }
}
