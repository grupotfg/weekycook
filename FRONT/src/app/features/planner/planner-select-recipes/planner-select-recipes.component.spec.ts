import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlannerSelectRecipesComponent } from './planner-select-recipes.component';

describe('PlannerSelectRecipesComponent', () => {
  let component: PlannerSelectRecipesComponent;
  let fixture: ComponentFixture<PlannerSelectRecipesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlannerSelectRecipesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PlannerSelectRecipesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
