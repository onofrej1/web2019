<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Result extends Model
{
    protected $table = 'run_result';

    public function runner()
    {
        return $this->belongsTo(Runner::class, 'runner_id');
    }

}
