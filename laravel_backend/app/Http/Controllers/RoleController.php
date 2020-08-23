<?php

namespace App\Http\Controllers;

use Spatie\Permission\Models\Role;

class RoleController extends ResourceController
{
    public $model = 'Spatie\Permission\Models\Role';

    public function indexxxx() {
        $role = new $this->model();

        return $role->all();
    }
}
