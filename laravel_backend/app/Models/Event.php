<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Event extends Model
{
    protected $table = 'run_event';

    public function run()
    {
        return $this->belongsTo(Run::class);
    }
}
