import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <header style="background:#0f172a; color:#fff;">
      <div class="container row-wrap" style="justify-content: space-between; align-items: center;">
        <h1 style="font-size: 18px;">BIP Frontend</h1>
        <nav class="row-wrap">
          <a routerLink="/beneficios" routerLinkActive="active" style="color:#fff; text-decoration:none;">Benefícios</a>
          <a routerLink="/transferencias" routerLinkActive="active" style="color:#fff; text-decoration:none;">Transferências</a>
        </nav>
      </div>
    </header>

    <main>
      <router-outlet></router-outlet>
    </main>
  `
})
export class AppComponent {}

