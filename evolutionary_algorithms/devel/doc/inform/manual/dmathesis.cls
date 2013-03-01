% This is the dmathesis class that gives a format of the thesis
% according to the university of Durham regulation.  To use it, say
% \documentclass{dmathesis} 

% You need to supply file format.tex, where you define the packages
% you want to use etc.

% Good luck and save your time!

% Written by M. Imran and last modified on 2001/06/18.

\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{dmathesis}[2001/06/18 Durham Maths thesis class]

%  Here the fontsize you can use.

\newcommand\@dmathesisptsize{12pt}

\DeclareOption{12pt}{\renewcommand\@dmathesisptsize{12pt}}
\DeclareOption{11pt}{\renewcommand\@dmathesisptsize{11pt}}
\DeclareOption{10pt}{\renewcommand\@dmathesisptsize{10pt}}


\ProcessOptions

\LoadClass[\@dmathesisptsize,a4paper,twoside]{report}

\pagestyle{plain}

%  Here the page setup based on the university of Durham regulation 

\setlength{\voffset}{-1in}  
\setlength{\marginparsep}{0in}  
\setlength{\marginparwidth}{0in}  
\setlength{\marginparpush}{0in}  

\setlength{\hoffset}{-1in}  
\setlength{\oddsidemargin}{4cm}  
\setlength{\evensidemargin}{2.5cm} 
\setlength{\textwidth}{14.5cm}
\setlength{\topmargin}{2cm}
\setlength{\headheight}{1cm}
\setlength{\headsep}{1cm}
\setlength{\textheight}{21.7cm}
\setlength{\footskip}{18pt}

%  Redefinition  \tableofcontents  in  report.cls
\makeatletter
\renewcommand\tableofcontents{%
    \if@twocolumn
      \@restonecoltrue\onecolumn
    \else
      \@restonecolfalse
    \fi
    \chapter*{\contentsname
        \@mkboth{\contentsname}{\contentsname}}%
    \@starttoc{toc}%
    \if@restonecol\twocolumn\fi
    }

%  Redefinition \listoffigures in report.cls
\renewcommand\listoffigures{%
    \if@twocolumn
      \@restonecoltrue\onecolumn
    \else
      \@restonecolfalse
    \fi
    \chapter*{\listfigurename
      \@mkboth{\listfigurename}{\listfigurename}}%
    \@starttoc{lof}%
    \if@restonecol\twocolumn\fi
    }

% %  Redefinition \listoftables in report.cls
\renewcommand\listoftables{%
    \if@twocolumn
      \@restonecoltrue\onecolumn
    \else
      \@restonecolfalse
    \fi
    \chapter*{\listtablename
      \@mkboth{\listtablename}{\listtablename}}%
    \@starttoc{lot}%
    \if@restonecol\twocolumn\fi
    }

%  Redefinition thebibliograph in report.cls
\renewenvironment{thebibliography}[1]
     {\chapter*{\bibname
        \@mkboth{\bibname}{\bibname}}%
      \list{\@biblabel{\@arabic\c@enumiv}}%
           {\settowidth\labelwidth{\@biblabel{#1}}%
            \leftmargin\labelwidth
            \advance\leftmargin\labelsep
            \@openbib@code
            \usecounter{enumiv}%
            \let\p@enumiv\@empty
            \renewcommand\theenumiv{\@arabic\c@enumiv}}%
      \sloppy
      \clubpenalty4000
      \@clubpenalty \clubpenalty
      \widowpenalty4000%
      \sfcode`\.\@m}
     {\def\@noitemerr
       {\@latex@warning{Empty `thebibliography' environment}}%
      \endlist}




%  Input the format.tex file.
%\input format.tex


%%% Clear Header %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Clear Header Style on the Last Empty Odd pages
\makeatletter
\def\cleardoublepage{\clearpage\if@twoside \ifodd\c@page\else%
    \hbox{}%
    \thispagestyle{empty}%              % Empty header styles
    \newpage%
    \if@twocolumn\hbox{}\newpage\fi\fi\fi}
\makeatother
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
 
%\endinput















