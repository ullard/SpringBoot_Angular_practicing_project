import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SettingsTfaComponent } from './settings-tfa.component';

describe('SettingsTfaComponent', () => {
  let component: SettingsTfaComponent;
  let fixture: ComponentFixture<SettingsTfaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SettingsTfaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SettingsTfaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
