<?php

namespace App\Http\Controllers;

use App\Models\Runner;
use Illuminate\Support\Facades\Log;

class RunnerController extends ResourceController
{
    public $model = 'App\Models\Runner';

    function checkNames() {
        $runners = request('runners');
        $data = collect();

        foreach ($runners as $runner) {
            Log::info($runner);

            $r = Runner::where('first_name', $runner['first_name'])
                ->where('last_name', $runner['last_name'])
                ->first();

            if($r) {
                $data->push($r);
                continue;
            } else {
                $r = new \stdClass();
            }

            foreach (Runner::all() as $rr) {
                $lev = levenshtein($runner['last_name'], $rr->last_name);
                if($lev > 2) continue;


                $data->push($r);
            }
        }

        return $data;
    }
}
