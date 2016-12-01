" To use it, copy it to
"     for Unix and OS/2:  ~/.vimrc
"	      for Amiga:  s:.vimrc
"  for MS-DOS and Win32:  $VIM\_vimrc
"	    for OpenVMS:  sys$login:.vimrc

" When started as "evim", evim.vim will already have done these settings.
if v:progname =~? "evim"
  finish
endif

" Use Vim settings, rather than Vi settings (much better!).
" This must be first, because it changes other options as a side effect.
set nocompatible
set t_Co=256
" allow backspacing over everything in insert mode
set backspace=indent,eol,start

"if has("vms")
  "set nobackup		" do not keep a backup file, use versions instead
"else
  "set backup		" keep a backup file
"endif
set history=50		" keep 50 lines of command line history
set ruler		" show the cursor position all the time
set showcmd		" display incomplete commands
set incsearch		" do incremental searching

" For Win32 GUI: remove 't' flag from 'guioptions': no tearoff menu entries
" let &guioptions = substitute(&guioptions, "t", "", "g")

" Don't use Ex mode, use Q for formatting
map Q gq

" CTRL-U in insert mode deletes a lot.  Use CTRL-G u to first break undo,
" so that you can undo CTRL-U after inserting a line break.
inoremap <C-U> <C-G>u<C-U>

" In many terminal emulators the mouse works just fine, thus enable it.
if has('mouse')
  set mouse=a
endif

" Switch syntax highlighting on, when the terminal has colors
" Also switch on highlighting the last used search pattern.
if &t_Co > 2 || has("gui_running")
  syntax on
  syntax enable
  set hlsearch
endif

" Only do this part when compiled with support for autocommands.
if has("autocmd")

  " Enable file type detection.
  " Use the default filetype settings, so that mail gets 'tw' set to 72,
  " 'cindent' is on in C files, etc.
  " Also load indent files, to automatically do language-dependent indenting.
  filetype plugin indent on

  " Put these in an autocmd group, so that we can delete them easily.
  augroup vimrcEx
  au!

  " For all text files set 'textwidth' to 78 characters.
  autocmd FileType text setlocal textwidth=100

  " When editing a file, always jump to the last known cursor position.
  " Don't do it when the position is invalid or when inside an event handler
  " (happens when dropping a file on gvim).
  " Also don't do it when the mark is in the first line, that is the default
  " position when opening a file.
  autocmd BufReadPost *
    \ if line("'\"") > 1 && line("'\"") <= line("$") |
    \   exe "normal! g`\"" |
    \ endif

  augroup END

else

  set autoindent		" always set autoindenting on

endif " has("autocmd")

" Convenient command to see the difference between the current buffer and the
" file it was loaded from, thus the changes you made.
" Only define it when not defined already.
if !exists(":DiffOrig")
  command DiffOrig vert new | set bt=nofile | r ++edit # | 0d_ | diffthis
		  \ | wincmd p | diffthis
endif
" =============================================================================
" ----------------Rugal Bernstein's personal vim Configuration----------------
" ============================================================================

" little tips
set nu
set cursorline
set autoindent
set tabstop=4
set shiftwidth=4
set expandtab
set textwidth=0
set softtabstop=4
"set noexpandtab
set autoindent
set ff=unix
set foldmethod=indent
set list
set listchars=eol:$,tab:>-,trail:~,extends:>,precedes:< "set indent chars list
set ic
"set spell
"set spelllang=en
"set noignorecase
"autocmd FileType python set omnifunc=pythoncomplete#Complete
"autocmd FileType c set omnifunc=ccomplete#Complete
set ofu=syntaxcomplete#Complete
if !has("gui_running")
    nmap mx :!cmatrix<cr>
endif


" ---------------------------------vundle configure
"  git clone https://github.com/gmarik/Vundle.vim.git ~/.vim/bundle/Vundle.vim
set rtp+=~/.vim/bundle/Vundle.vim/
call vundle#begin()
Plugin 'gmarik/vundle'
Plugin 'taglist.vim'
Plugin 'winmanager'
Plugin 'fholgado/minibufexpl.vim'
Plugin 'SuperTab'
Plugin 'The-NERD-tree'
Plugin 'The-NERD-Commenter'
Plugin 'mru.vim'
Plugin 'JavaDecompiler.vim'
Plugin 'Lokaltog/powerline',{'rtp':'powerline/bindings/vim'}
Plugin 'Lokaltog/vim-easymotion'
Plugin 'Townk/vim-autoclose'
Plugin 'Yggdroot/indentLine'
Plugin 'vimomni'
Plugin 'altercation/vim-colors-solarized'
Plugin 'Valloric/YouCompleteMe'
Plugin 'tpope/vim-fireplace'
Plugin 'tpope/vim-salve'
Plugin 'tpope/vim-dispatch'
Plugin 'vim-scripts/VimClojure'
Plugin 'bhurlow/vim-parinfer'
Plugin 'ShowTrailingWhitespace'
Plugin 'mhinz/vim-startify'
Plugin 'kien/rainbow_parentheses.vim'
call vundle#end()


" wildmenu configure
set wildmenu
set wildmode=list:longest          " 补全时显示样式为多行
set wildignore+=.hg,.git,.svn                    " 版本控制
set wildignore+=*.pyc                            " Python 字节码
set wildignore+=*.sw?                            " Vim 交换文件
set wildignore+=*.aux,*.out,*.toc                " LaTeX 文件
set wildignore+=*.jpg,*.bmp,*.gif,*.png,*.jpeg   " 二进制图像
set wildignore+=*.o,*.obj,*.exe,*.dll,*.manifest " 已编译的对象文件
set wildignore+=*.luac                           " Lua 字节码
set wildignore+=*.DS_Store                       " OSX 糟糕物

" Ctags configure
map <F12> :!ctags -R --c++-kinds=+p --fields=+iaS --extra=+q .<CR>

" taglist.vim configure
let Tlist_Ctags_Cmd='/usr/bin/ctags'
let Tlist_Show_One_File=1
let Tlist_Exit_OnlyWindow=1
let Tlist_Use_Right_Window=0
let Tlist_File_Fold_Auto_Close=1
let Tlist_Auto_Open = 0
let Tlist_Sort_Type='name'
let Tlist_Show_Menu=1
let Tlist_Max_Submenu_Items=10
let Tlist_Max_Tag_length=50
let Tlist_Use_SingleClick=0
let Tlist_Use_SingleClick=0
let Tlist_File_Fold_Auto_Close=1
let Tlist_GainFocus_On_ToggleOpen=0
let Tlist_Use_Horiz_Window=0
:command -range=% T :TlistToggle 

" winmanager configure
"let g:winManagerWindowLayout='FileExplorer|TagList'
nmap wm :WMToggle<cr>
nmap <F12> wm

" quickfix configure
nmap <F6> :cn<cr>
nmap <F7> :cp<cr>

" minibufexpl.vim configure
" minibufexpl.vim configure
noremap <C-J>     <C-W>j
noremap <C-K>     <C-W>k
noremap <C-H>     <C-W>h
noremap <C-L>     <C-W>l
noremap <C-Tab>   :MBEbn<CR>
noremap <C-S-Left>    :MBEbp<CR>

" omnicomplete configure 
set completeopt=menu,longest


" SuperTab configure
let g:SuperTabRetainCompletionType=2
let g:SuperTabDefaultCompletionType="<C-x><C-o>"

" The-NERD-Tree configure
let g:NERDTree_title="[NERDTree]"  
let g:winManagerWindowLayout="NERDTree|TagList"  
function! NERDTree_Start()  
	exec 'NERDTree'  
endfunction  
function! NERDTree_IsValid()  
	return 1  
endfunction  

" Pydiction configure
let g:pydiction_location='~/.vim/bundle/Pydiction/complete-dict'

" powerline configure
set laststatus=2
set noshowmode "hide default mode text

" mru.vim configure
"let MRU_File='~/.mru_files'
let MRU_Max_Extries=200
let MRU_Exclude_Files='^/tmp/.*\|^/var/tmp/.*'
let MRU_auto_Close=1
let MRU_Use_Current_Window=0
nmap mru  :MRU<CR>

" YouCompleteMe configuration
"let g:ycm_global_ycm_extra_conf = '~/.vim/bundle/YouCompleteMe/cpp/ycm/.ycm_extra_conf.py'

" VimClojure configuration
let g:vimclojure#ParenRainbow=10

"strip all trailing whitespace in the current file
nnoremap <leader>w :%s/\s\+$//<cr>:let @/=''<CR>
