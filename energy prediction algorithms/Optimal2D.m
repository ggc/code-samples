% Radiations sample (From AEMET) [ºC]
Tamb = importdata('Tamb_matrix.txt', '\t', 1);
D = size(Tamb.data,1);
T = size(Tamb.data,2);
% for i = [4, 7]
%     disp(Tamb.textdata{i, 1})
%     disp(Tamb.data(i, :))
%     disp(' ')
% end

%% Constants
% Not used
% a = 1; % area 1m2
% pr = 0.75; %% default ratio
% solarradavg = 7.7/30/4 * 1000; % 3.1 feb (7.2)7.7(8.1) jul
% r = 0.05; %% adjust
Ppv_stc = 165; % Nominal PV power [W]
Gt = 1000; % Irradiation amount [W/m^2]
y = 0.00043; % Temperature coefficient [%/ºC]
NOCT = 45.5; % Nominal Operating Cell Temperature [ºC]
Npvp = 2; % Parallel cells
Npvs = 2; % Series cells

% Weghting constant
a1 = 0.1;
a2 = 0.3;
a3 = 0.6;

%% Algorithm ecuations
% Cell temperature [ºC]
Tj = @(Ta) Ta + (Gt/800) * NOCT - 20;

% #CHECK Photovoltaic power [W]
Ppv = @(Ta) (( Ppv_stc * (Gt/1000) * (1 - y*(Tj(Ta) - 25)) ) * Npvs * Npvp);

%% Processing values
% Initialize first predicted day with real values
X = zeros(31,4); % Predicted power
% for i = 1:4
%         X(1,i) = Ppv(Tamb.data(1,i));
%         disp(Tamb.textdata(i, 1))
%         disp(X(1,i));
% end
Tamb_t = Tamb.data';
% Fill predicted values with Algorithm output
for d = 3:D
    for t = 1:T
        pos = (d-1)*T + t;
        X(d, t) = a1*Ppv( Tamb_t(pos-T-1) ) + a2*Ppv( Tamb_t(pos-T) ) + a3*Ppv( Tamb_t(pos-1) );
    end
end

disp(X);
% fprintf('%s W per cell\n', num2str(Ppv(Tamb.data(1,4)),'%.2f'));



