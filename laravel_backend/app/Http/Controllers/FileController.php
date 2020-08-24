<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Http\Requests;
use Storage;

class FileController extends Controller {

    public function getFiles() {
        $baseUrl = 'http://localhost:8000';

        $files = collect(Storage::files('public/uploads'))->map(function($file) use($baseUrl) {
            $f = new \stdClass();
            $f->path = $baseUrl.'/storage/uploads/'.basename($file);
            $f->name = basename($file);
            return $f;
        });

        return $files;
    }

    public function uploadFile(Request $request) {
        $fileName = time().'_'.$request->myfile->getClientOriginalName();
        $filePath = $request->file('myfile')->storeAs('public/uploads', $fileName);

        return $request->file;
    }
}
