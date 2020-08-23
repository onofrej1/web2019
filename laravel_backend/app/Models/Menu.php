<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Menu extends Model
{
    protected $table = 'menus';

    public $timestamps = false;

    public function menuItems()
    {
        return $this->hasMany(MenuItem::class);
    }

}
