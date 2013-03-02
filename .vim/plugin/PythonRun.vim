"--- python configure this map for conveniently run python file
map <F4> :!clear;python %<CR>
if exists("compiler")
    finish
endif
let compiler = "python"
setlocal makeprg=python
setlocal errorformat=%C\ %.%#,%A\ \ File\ \"%f\"\\,\ line\ %l%.%#,%Z%[%^\ ]%\\@=%m
au! BufWriteCmd *.py call CheckPythonSyntax()
function CheckPythonSyntax()
    let curfile = bufname("%")
    let tmpfile = tempname()
    silent execute "write! ".tmpfile
    let output = system("python -c \"__import__('py_compile').compile(r'".tmpfile."')\" 2>&1")
    if output != ''
        let output = substitute(output, fnameescape(tmpfile), fnameescape(curfile), "g")
        echo output
    else
        write
    endif
    call delete(tmpfile)
endfunction
